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

import io.github.nobuglady.network.fw.constant.NodeStatus;
import io.github.nobuglady.network.fw.constant.NodeStatusDetail;
import io.github.nobuglady.network.fw.marker.helper.FlowHelper;
import io.github.nobuglady.network.fw.marker.helper.model.FlowHelperModel;
import io.github.nobuglady.network.fw.persistance.FlowContainer;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;
import io.github.nobuglady.network.fw.queue.complete.CompleteNodeResult;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowMarker {

	private static FlowHelper flowHelper = new FlowHelper();

	/**
	 * 
	 * @param nodeResult
	 */
	public static void onNodeComplete(CompleteNodeResult nodeResult) {

		String flowId = nodeResult.getFlowId();
		String historyId = nodeResult.getHistoryId();
		String nodeId = nodeResult.getNodeId();

		HistoryNodeEntity historyNodeEntity = FlowContainer.selectNodeByKey(flowId, nodeId, historyId);

		if (NodeStatus.COMPLETE != historyNodeEntity.getNodeStatus()) {
			return;
		}

		if (NodeStatus.COMPLETE == historyNodeEntity.getNodeStatus()
				&& NodeStatusDetail.COMPLETE_SUCCESS != historyNodeEntity.getNodeStatusDetail()) {
			// has error
			return;
		}

		FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeId, NodeStatus.GO);

		// mark next
		markNext(flowId, historyId, nodeId);

	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeId
	 */
	private static void markNext(String flowId, String historyId, String nodeId) {

		FlowHelperModel flow = flowHelper.getFlow(flowId, historyId);

		Map<String, List<HistoryEdgeEntity>> edgesMap = flow.getEdgesMap();
		Map<String, HistoryNodeEntity> nodeMap = flow.getNodeMap();

		List<HistoryEdgeEntity> edgeList = edgesMap.get(nodeId);
		if (edgeList != null && edgeList.size() > 0) {
			for (HistoryEdgeEntity edge : edgeList) {
				HistoryNodeEntity nodeTo = nodeMap.get(edge.getToNodeId());

				if (nodeTo == null) {
					continue;
				}

				boolean needWait = false;
				Map<String, List<HistoryEdgeEntity>> edgesBackMap = flow.getEdgesBackMap();
				List<HistoryEdgeEntity> edgeBackList = edgesBackMap.get(nodeTo.getNodeId());
				for (HistoryEdgeEntity flowEdgeBack : edgeBackList) {
					HistoryNodeEntity nodeFrom = nodeMap.get(flowEdgeBack.getFromNodeId());
					if (!(NodeStatus.GO == nodeFrom.getNodeStatus())) {
						needWait = true;
					}
				}

				if (!needWait) {
					FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(), NodeStatus.READY);
				}

			}
		}
	}

}
