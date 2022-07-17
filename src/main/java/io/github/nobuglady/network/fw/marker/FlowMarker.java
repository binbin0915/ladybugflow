/*
 * Copyright (c) 2021-present, NoBugLady Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package io.github.nobuglady.network.fw.marker;

import java.util.List;
import java.util.Map;

import io.github.nobuglady.network.fw.constant.EdgeStatus;
import io.github.nobuglady.network.fw.constant.NodeReadyCheck;
import io.github.nobuglady.network.fw.constant.NodeStatus;
import io.github.nobuglady.network.fw.constant.NodeStatusDetail;
import io.github.nobuglady.network.fw.marker.helper.FlowHelper;
import io.github.nobuglady.network.fw.marker.helper.model.FlowHelperModel;
import io.github.nobuglady.network.fw.persistance.FlowContainer;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;
import io.github.nobuglady.network.fw.queue.complete.CompleteNodeResult;
import io.github.nobuglady.network.fw.util.StringUtil;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowMarker {

	private static FlowHelper flowHelper = new FlowHelper();

	/**
	 * onNodeComplete
	 * 
	 * @param nodeResult nodeResult
	 * @return true:check flow status
	 */
	public static boolean onNodeComplete(CompleteNodeResult nodeResult) {

		String flowId = nodeResult.getFlowId();
		String historyId = nodeResult.getHistoryId();
		String nodeId = nodeResult.getNodeId();
		int nodeStatusDetail = nodeResult.getNodeStatus();
		String returnValue = nodeResult.getNodeResult();

		if (returnValue != null) {
			FlowContainer.updateNodeReturnValueByNodeId(flowId, historyId, nodeId, returnValue);
		}
		FlowContainer.updateNodeStatusDetailByNodeId(flowId, historyId, nodeId, NodeStatus.COMPLETE, nodeStatusDetail);

		HistoryNodeEntity historyNodeEntity = FlowContainer.selectNodeByKey(flowId, nodeId, historyId);
		if (historyNodeEntity == null) {
			return false;
		}

		if (NodeStatus.COMPLETE != historyNodeEntity.getNodeStatus()) {
			return true;
		}

		FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeId, NodeStatus.GO);

		// mark next
		return markNext(flowId, historyId, nodeId);

	}

	/**
	 * markNext
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @param nodeId    nodeId
	 * @return true:check flow status
	 */
	private static boolean markNext(String flowId, String historyId, String nodeId) {

		FlowHelperModel flow = flowHelper.getFlow(flowId, historyId);

		Map<String, List<HistoryEdgeEntity>> edgesMap = flow.getEdgesMap();
		Map<String, HistoryNodeEntity> nodeMap = flow.getNodeMap();

		List<HistoryEdgeEntity> edgeList = edgesMap.get(nodeId);

		// update next
		if (edgeList != null && edgeList.size() > 0) {
			for (HistoryEdgeEntity edge : edgeList) {

				HistoryNodeEntity nodeFrom = nodeMap.get(edge.getFromNodeId());
				HistoryNodeEntity nodeTo = nodeMap.get(edge.getToNodeId());

				if (nodeTo == null) {
					continue;
				}

				int edgeStatus = checkCondition(nodeFrom.getNodeStatusDetail(), nodeFrom.getReturnValue(),
						edge.getEdgeCondition());
				edge.setEdgeStatus(edgeStatus);
				FlowContainer.updateEdgeStatusByKey(edge.getFlowId(), edge.getHistoryId(), edge.getEdgeId(),
						edgeStatus);

				boolean needWait = false;
				boolean hasError = false;
				boolean hasOk = false;
				Map<String, List<HistoryEdgeEntity>> edgesBackMap = flow.getEdgesBackMap();
				List<HistoryEdgeEntity> edgeBackList = edgesBackMap.get(nodeTo.getNodeId());
				for (HistoryEdgeEntity flowEdgeBack : edgeBackList) {

					if (EdgeStatus.INIT == flowEdgeBack.getEdgeStatus()) {

						needWait = true;

					} else {

						if (EdgeStatus.ERROR == flowEdgeBack.getEdgeStatus()) {
							hasError = true;
						}

						if (EdgeStatus.OK == flowEdgeBack.getEdgeStatus()) {
							hasOk = true;
						}

					}

				}

				if (!needWait) {
					if (hasError) {
						FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(), NodeStatus.INIT);
					} else {
						if (hasOk) {

							HistoryNodeEntity historyNodeEntity = FlowContainer.selectNodeByKey(flowId,
									nodeTo.getNodeId(), historyId);
							if (historyNodeEntity.getNodeStatus() == NodeStatus.WAIT
									|| historyNodeEntity.getNodeStatus() == NodeStatus.INIT) {
								FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(),
										NodeStatus.READY);
							} else {
								return false;
							}

						} else {
							// all ng, do nothing
						}

					}

				} else {

					if (NodeReadyCheck.CHECK_SINGLE == nodeTo.getReadyCheck()) {
						if (hasOk) {

							HistoryNodeEntity historyNodeEntity = FlowContainer.selectNodeByKey(flowId,
									nodeTo.getNodeId(), historyId);
							if (historyNodeEntity.getNodeStatus() == NodeStatus.WAIT
									|| historyNodeEntity.getNodeStatus() == NodeStatus.INIT) {
								FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(),
										NodeStatus.READY);
							} else {
								return false;
							}

						} else {
							FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(),
									NodeStatus.WAIT);
						}
					} else {
						FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(), NodeStatus.WAIT);
					}

				}

			}
		}

		return true;
	}

	/**
	 * checkCondition
	 * 
	 * @param nodeStatusDetail nodeStatusDetail
	 * @param returnValue      returnValue
	 * @param edgeCondition    edgeCondition
	 * @return edgeStatus
	 */
	private static int checkCondition(int nodeStatusDetail, String returnValue, String edgeCondition) {

		if (!(NodeStatusDetail.COMPLETE_SUCCESS == nodeStatusDetail)) {
			return EdgeStatus.ERROR;
		}

		if (StringUtil.isEmpty(edgeCondition)) {
			return EdgeStatus.OK;
		}

		if (String.valueOf(returnValue).equals(edgeCondition)) {
			return EdgeStatus.OK;
		}

		return EdgeStatus.NG;
	}

}
