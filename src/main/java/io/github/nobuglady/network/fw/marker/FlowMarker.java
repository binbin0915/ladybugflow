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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.nobuglady.network.fw.component.FlowComponentFactory;
import io.github.nobuglady.network.fw.component.IFlowAccessor;
import io.github.nobuglady.network.fw.component.IFlowMarker;
import io.github.nobuglady.network.fw.constant.EdgeStatus;
import io.github.nobuglady.network.fw.constant.FlowStatus;
import io.github.nobuglady.network.fw.constant.NodeReadyCheck;
import io.github.nobuglady.network.fw.constant.NodeStatus;
import io.github.nobuglady.network.fw.constant.NodeStatusDetail;
import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.marker.helper.FlowHelper;
import io.github.nobuglady.network.fw.marker.helper.model.FlowHelperModel;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;
import io.github.nobuglady.network.fw.queue.complete.CompleteNodeResult;
import io.github.nobuglady.network.fw.starter.FlowStarter;
import io.github.nobuglady.network.fw.util.FlowUtil;
import io.github.nobuglady.network.fw.util.StringUtil;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowMarker implements IFlowMarker {

	private FlowHelper flowHelper = new FlowHelper();

	private IFlowAccessor flowAccessor = FlowComponentFactory.getFlowAccessor();

	/**
	 * onNodeComplete
	 * 
	 * @param nodeResult nodeResult
	 */
	public void onNodeComplete(CompleteNodeResult nodeResult) {

		String flowId = nodeResult.getFlowId();
		String historyId = nodeResult.getHistoryId();

		ConsoleLogger logger = ConsoleLogger.getInstance(flowId, historyId);

		try {
			boolean markResult = mark(nodeResult);

			if (!markResult) {
				return;
			}

			List<HistoryNodeEntity> readyNodeList = getReadyNode(flowId, historyId);

			if (readyNodeList.size() > 0) {
				for (HistoryNodeEntity readyNode : readyNodeList) {
					startNode(flowId, historyId, readyNode.getNodeId());
				}
			} else {

				List<HistoryNodeEntity> runningNodeList = getRunningNode(flowId, historyId);
				List<HistoryNodeEntity> openingNodeList = getOpenningNode(flowId, historyId);
				List<HistoryNodeEntity> waitingNodeList = getWaitingNode(flowId, historyId);
				List<HistoryNodeEntity> errorNodeList = getErrorNode(flowId, historyId);

				if (runningNodeList.size() == 0 && openingNodeList.size() == 0 && waitingNodeList.size() == 0) {

					if (errorNodeList.size() > 0) {
						logger.info("Complete error.");
						updateFlowStatus(flowId, historyId, true);
					} else {
						logger.info("Complete success.");
						updateFlowStatus(flowId, historyId, false);
					}

					logger.info("json:\n" + FlowUtil.dumpJson(flowId, historyId));

					if (errorNodeList.size() > 0) {
						FlowStarter.flowRunnerMap.get(flowId + "," + historyId).putComplete("ERROR");
						if (FlowStarter.deleteOnError) {
							flowAccessor.removeFlow(flowId, historyId);
							FlowStarter.flowRunnerMap.remove(flowId + "," + historyId);
						}
					} else {
						FlowStarter.flowRunnerMap.get(flowId + "," + historyId).putComplete("SUCCESS");
						if (FlowStarter.deleteOnComplete) {
							flowAccessor.removeFlow(flowId, historyId);
							FlowStarter.flowRunnerMap.remove(flowId + "," + historyId);
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			updateFlowStatus(flowId, historyId, true);
		}

	}

	/**
	 * mark
	 * 
	 * @param nodeResult nodeResult
	 * @return true:check flow status
	 */
	public boolean mark(CompleteNodeResult nodeResult) {

		String flowId = nodeResult.getFlowId();
		String historyId = nodeResult.getHistoryId();
		String nodeId = nodeResult.getNodeId();
		int nodeStatusDetail = nodeResult.getNodeStatus();
		String returnValue = nodeResult.getNodeResult();

		HistoryNodeEntity historyNodeEntity = flowAccessor.selectNodeByKey(flowId, nodeId, historyId);
		if (historyNodeEntity == null) {
			return false;
		}

		if (returnValue != null) {
			flowAccessor.updateNodeReturnValueByNodeId(flowId, historyId, nodeId, returnValue);
		}
		flowAccessor.updateNodeStatusDetailByNodeId(flowId, historyId, nodeId, NodeStatus.COMPLETE, nodeStatusDetail);

		flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeId, NodeStatus.GO);

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
	private boolean markNext(String flowId, String historyId, String nodeId) {

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
				flowAccessor.updateEdgeStatusByKey(edge.getFlowId(), edge.getHistoryId(), edge.getEdgeId(), edgeStatus);
				edge.setEdgeStatus(edgeStatus);

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
						flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(), NodeStatus.INIT);
						nodeTo.setNodeStatus(NodeStatus.INIT);
					} else {
						if (hasOk) {

							HistoryNodeEntity historyNodeEntity = flowAccessor.selectNodeByKey(flowId,
									nodeTo.getNodeId(), historyId);
							if (historyNodeEntity.getNodeStatus() == NodeStatus.WAIT
									|| historyNodeEntity.getNodeStatus() == NodeStatus.INIT) {
								flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(),
										NodeStatus.READY);
								nodeTo.setNodeStatus(NodeStatus.READY);
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

							HistoryNodeEntity historyNodeEntity = flowAccessor.selectNodeByKey(flowId,
									nodeTo.getNodeId(), historyId);
							if (historyNodeEntity.getNodeStatus() == NodeStatus.WAIT
									|| historyNodeEntity.getNodeStatus() == NodeStatus.INIT) {
								flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(),
										NodeStatus.READY);
								nodeTo.setNodeStatus(NodeStatus.READY);
							} else {
								return false;
							}

						} else {
							flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(),
									NodeStatus.WAIT);
							nodeTo.setNodeStatus(NodeStatus.WAIT);
						}
					} else {
						flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeTo.getNodeId(), NodeStatus.WAIT);
						nodeTo.setNodeStatus(NodeStatus.WAIT);
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
	private int checkCondition(int nodeStatusDetail, String returnValue, String edgeCondition) {

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

	////////////////////////////////////////////////////////
	// shutdown
	////////////////////////////////////////////////////////

	/**
	 * updateFlowStatus
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @param hasError  hasError
	 */
	public void updateFlowStatus(String flowId, String historyId, boolean hasError) {

		if (hasError) {
			flowAccessor.updateFlowStatus(flowId, historyId, FlowStatus.ERROR);
		} else {
			flowAccessor.updateFlowStatus(flowId, historyId, FlowStatus.COMPLETE);
		}
	}

	/**
	 * startNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @param nodeId    nodeId
	 */
	private void startNode(String flowId, String historyId, String nodeId) {

		flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeId, NodeStatus.RUNNING);
		FlowComponentFactory.getReadyQueueSender().putReadyNode(flowId, historyId, nodeId);
	}

	/**
	 * getReadyNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	private List<HistoryNodeEntity> getReadyNode(String flowId, String historyId) {

		List<HistoryNodeEntity> result = new ArrayList<>();

		List<HistoryNodeEntity> result_ready = flowAccessor.selectNodeListByStatus(flowId, historyId, NodeStatus.READY);

		if (result_ready != null) {
			result.addAll(result_ready);
		}
		return result;
	}

	/**
	 * getRunningNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	private List<HistoryNodeEntity> getRunningNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = flowAccessor.selectNodeListByStatus(flowId, historyId, NodeStatus.RUNNING);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	/**
	 * getOpenningNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	private List<HistoryNodeEntity> getOpenningNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = flowAccessor.selectNodeListByStatus(flowId, historyId, NodeStatus.OPENNING);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	/**
	 * getWaitingNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	private List<HistoryNodeEntity> getWaitingNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = flowAccessor.selectNodeListByStatus(flowId, historyId, NodeStatus.WAIT);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	/**
	 * getErrorNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	private List<HistoryNodeEntity> getErrorNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = flowAccessor.selectNodeListByStatusDetail(flowId, historyId,
				NodeStatus.COMPLETE, NodeStatusDetail.COMPLETE_ERROR);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

}
