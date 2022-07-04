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
package io.github.nobuglady.network.fw;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.nobuglady.network.fw.constant.FlowStatus;
import io.github.nobuglady.network.fw.constant.NodeStatus;
import io.github.nobuglady.network.fw.constant.NodeStatusDetail;
import io.github.nobuglady.network.fw.marker.FlowMarker;
import io.github.nobuglady.network.fw.model.EdgeDto;
import io.github.nobuglady.network.fw.model.FlowDto;
import io.github.nobuglady.network.fw.model.NodeDto;
import io.github.nobuglady.network.fw.persistance.FlowContainer;
import io.github.nobuglady.network.fw.persistance.entity.FlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;
import io.github.nobuglady.network.fw.queue.complete.CompleteNodeResult;
import io.github.nobuglady.network.fw.queue.ready.ReadyQueueManager;
import io.github.nobuglady.network.fw.util.FlowUtil;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowManager {

	/**
	 * 
	 * @param nodeResult
	 */
	public static void onNodeComplete(CompleteNodeResult nodeResult) {

		String flowId = nodeResult.getFlowId();
		String historyId = nodeResult.getHistoryId();

		try {
			boolean markResult = FlowMarker.onNodeComplete(nodeResult);

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
						System.out.println("Complete error.");
						updateFlowStatus(flowId, historyId, true);
					} else {
						System.out.println("Complete success.");
						updateFlowStatus(flowId, historyId, false);
					}

					System.out.println("json:\n" + FlowUtil.dumpJson(flowId, historyId));
					FlowContainer.flowMap.remove(flowId + "," + historyId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			updateFlowStatus(flowId, historyId, true);
		}

	}

	/**
	 * 
	 * @param flowRunner
	 */
	public static void startFlow(FlowRunner flowRunner) {

		String flowPath = flowRunner.getClass().getName();

		FlowEntity flow = createHistory(flowPath);
		String flowId = flow.flowEntity.getFlowId();
		String historyId = flow.flowEntity.getHistoryId();

		FlowContainer.flowRunnerMap.put(flowId + "," + historyId, flowRunner);

		List<HistoryNodeEntity> firstNodeList = getFirstNodeId(flow);

		if (firstNodeList != null) {
			for (HistoryNodeEntity firstNode : firstNodeList) {
				String firstNodeId = firstNode.getNodeId();
				firstNode.setNodeStatus(NodeStatus.READY);
				startNode(flowId, historyId, firstNodeId);
			}
		}

	}

	/**
	 * 
	 * @param flowPath
	 * @return
	 */
	private static FlowEntity createHistory(String flowPath) {

		String historyId = FlowContainer.createHistoryId();

		FlowEntity flow = loadJson(flowPath, historyId);
		FlowContainer.flowMap.put(flow.flowEntity.getFlowId() + "," + historyId, flow);

		return flow;
	}

	/**
	 * 
	 * @param flowPath
	 * @param historyId
	 * @return
	 */
	private static FlowEntity loadJson(String flowPath, String historyId) {

		FlowEntity flowEntityDB = new FlowEntity();

		try (Reader reader = new InputStreamReader(
				FlowManager.class.getResourceAsStream("/" + flowPath.replace(".", "/") + ".json"))) {

			ObjectMapper mapper = new ObjectMapper();
			FlowDto flowDto = mapper.readValue(reader, FlowDto.class);

			HistoryFlowEntity flowEntity = new HistoryFlowEntity();
			flowEntity.setFlowId(flowDto.flowId);
			flowEntity.setHistoryId(historyId);

			flowEntityDB.flowEntity = flowEntity;

			if (flowDto.nodes != null) {
				for (NodeDto nodeDto : flowDto.nodes) {
					HistoryNodeEntity nodeEntity = new HistoryNodeEntity();
					nodeEntity.setHistoryId(historyId);
					nodeEntity.setFlowId(flowEntity.getFlowId());
					nodeEntity.setNodeId(nodeDto.id);
					nodeEntity.setNodeName(nodeDto.label);

					flowEntityDB.nodeEntityList.add(nodeEntity);
				}
			}

			if (flowDto.edges != null) {
				for (EdgeDto edgeDto : flowDto.edges) {
					HistoryEdgeEntity edgeEntity = new HistoryEdgeEntity();
					edgeEntity.setHistoryId(historyId);
					edgeEntity.setEdgeId(edgeDto.id);
					edgeEntity.setFromNodeId(edgeDto.from);
					edgeEntity.setToNodeId(edgeDto.to);
					flowEntityDB.edgeEntityList.add(edgeEntity);
				}

			}

			String json = mapper.writeValueAsString(flowDto);
			System.out.println("json:\n" + json);

			FlowContainer.saveFlow(flowEntityDB);
			return flowEntityDB;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	////////////////////////////////////////////////////////
	// shutdown
	////////////////////////////////////////////////////////

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param hasError
	 */
	public static void updateFlowStatus(String flowId, String historyId, boolean hasError) {

		if (hasError) {
			FlowContainer.updateFlowStatus(flowId, historyId, FlowStatus.ERROR);
		} else {
			FlowContainer.updateFlowStatus(flowId, historyId, FlowStatus.COMPLETE);
		}
	}

	//////////////////////////////
	// help function
	//////////////////////////////

	/**
	 * 
	 * @param flow
	 * @return
	 */
	private static List<HistoryNodeEntity> getFirstNodeId(FlowEntity flow) {

		List<HistoryNodeEntity> resultList = new ArrayList<>();

		resultList.addAll(flow.nodeEntityList);

		Set<String> toNodeIdSet = new HashSet<String>();
		for (HistoryEdgeEntity edgeEntity : flow.edgeEntityList) {
			toNodeIdSet.add(edgeEntity.getToNodeId());
		}

		for (int i = resultList.size() - 1; i >= 0; i--) {

			HistoryNodeEntity nodeEntity = resultList.get(i);

			if (toNodeIdSet.contains(nodeEntity.getNodeId())) {
				resultList.remove(i);
			}
		}
		return resultList;
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeId
	 */
	private static void startNode(String flowId, String historyId, String nodeId) {

		ReadyQueueManager.getInstance().putReadyNode(flowId, historyId, nodeId);
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	private static List<HistoryNodeEntity> getReadyNode(String flowId, String historyId) {

		List<HistoryNodeEntity> result = new ArrayList<>();

		List<HistoryNodeEntity> result_ready = FlowContainer.selectNodeListByStatus(flowId, historyId,
				NodeStatus.READY);

		if (result_ready != null) {
			result.addAll(result_ready);
		}
		return result;
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	private static List<HistoryNodeEntity> getRunningNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = FlowContainer.selectNodeListByStatus(flowId, historyId, NodeStatus.RUNNING);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	private static List<HistoryNodeEntity> getOpenningNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = FlowContainer.selectNodeListByStatus(flowId, historyId, NodeStatus.OPENNING);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	private static List<HistoryNodeEntity> getWaitingNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = FlowContainer.selectNodeListByStatus(flowId, historyId, NodeStatus.WAIT);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	private static List<HistoryNodeEntity> getErrorNode(String flowId, String historyId) {
		List<HistoryNodeEntity> result = FlowContainer.selectNodeListByStatusDetail(flowId, historyId,
				NodeStatus.COMPLETE, NodeStatusDetail.COMPLETE_ERROR);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

}
