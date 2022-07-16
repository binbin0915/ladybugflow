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
package io.github.nobuglady.network.fw.persistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.github.nobuglady.network.fw.FlowRunner;
import io.github.nobuglady.network.fw.persistance.entity.FlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowContainer {

	/** key: flowId, historyId */
	public static Map<String, FlowEntity> flowMap = new HashMap<>();

	/** key: flowId, historyId */
	public static Map<String, FlowRunner> flowRunnerMap = new HashMap<>();

	/**
	 * createHistoryId
	 * 
	 * @return HistoryId
	 */
	public static String createHistoryId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * selectEdgeByFlowHistoryId
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryEdgeEntity
	 */
	public static List<HistoryEdgeEntity> selectEdgeByFlowHistoryId(String flowId, String historyId) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			return flowEntity.edgeEntityList;
		} else {
			return new ArrayList<>();
		}

	}

	/**
	 * selectNodeByFlowHistoryId
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	public static List<HistoryNodeEntity> selectNodeByFlowHistoryId(String flowId, String historyId) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			return flowEntity.nodeEntityList;
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * selectFlowByKey
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryFlowEntity
	 */
	public static HistoryFlowEntity selectFlowByKey(String flowId, String historyId) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			return flowEntity.flowEntity;
		} else {
			return null;
		}
	}

	/**
	 * selectNodeByKey
	 * 
	 * @param flowId    flowId
	 * @param nodeId    nodeId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	public static HistoryNodeEntity selectNodeByKey(String flowId, String nodeId, String historyId) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					return nodeEntity;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * updateNodeStatusByNodeId
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param nodeId     nodeId
	 * @param nodeStatus nodeStatus
	 */
	public static void updateNodeStatusByNodeId(String flowId, String historyId, String nodeId, int nodeStatus) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					nodeEntity.setNodeStatus(nodeStatus);
				}
			}
		}
	}

	/**
	 * updateFlowStatus
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param flowStatus flowStatus
	 */
	public static void updateFlowStatus(String flowId, String historyId, int flowStatus) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			flowEntity.flowEntity.setFlowStatus(flowStatus);
		}
	}

	/**
	 * selectNodeListByStatus
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param nodeStatus nodeStatus
	 * @return HistoryNodeEntity
	 */
	public static List<HistoryNodeEntity> selectNodeListByStatus(String flowId, String historyId, int nodeStatus) {
		List<HistoryNodeEntity> resultList = new ArrayList<>();

		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeStatus() == nodeStatus) {
					resultList.add(nodeEntity);
				}
			}
		}

		return resultList;
	}

	/**
	 * selectNodeListByStatusDetail
	 * 
	 * @param flowId           flowId
	 * @param historyId        historyId
	 * @param nodeStatus       nodeStatus
	 * @param nodeStatusDetail nodeStatusDetail
	 * @return HistoryNodeEntity
	 */
	public static List<HistoryNodeEntity> selectNodeListByStatusDetail(String flowId, String historyId, int nodeStatus,
			int nodeStatusDetail) {
		List<HistoryNodeEntity> resultList = new ArrayList<>();

		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeStatus() == nodeStatus && nodeEntity.getNodeStatusDetail() == nodeStatusDetail) {
					resultList.add(nodeEntity);
				}
			}
		}

		return resultList;
	}

	/**
	 * updateNodeStatusDetailByNodeId
	 * 
	 * @param flowId           flowId
	 * @param historyId        historyId
	 * @param nodeId           nodeId
	 * @param nodeStatus       nodeStatus
	 * @param nodeStatusDetail nodeStatusDetail
	 */
	public static void updateNodeStatusDetailByNodeId(String flowId, String historyId, String nodeId, int nodeStatus,
			int nodeStatusDetail) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					nodeEntity.setNodeStatus(nodeStatus);
					nodeEntity.setNodeStatusDetail(nodeStatusDetail);
				}
			}
		}
	}

	/**
	 * updateNodeReturnValueByNodeId
	 * 
	 * @param flowId      flowId
	 * @param historyId   historyId
	 * @param nodeId      nodeId
	 * @param returnValue returnValue
	 */
	public static void updateNodeReturnValueByNodeId(String flowId, String historyId, String nodeId,
			String returnValue) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					nodeEntity.setReturnValue(returnValue);
				}
			}
		}
	}

	/**
	 * updateEdgeStatusByKey
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param edgeId     edgeId
	 * @param edgeStatus edgeStatus
	 */
	public static void updateEdgeStatusByKey(String flowId, String historyId, String edgeId, int edgeStatus) {
		FlowEntity flowEntity = FlowContainer.getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryEdgeEntity edgeEntity : flowEntity.edgeEntityList) {
				if (edgeEntity.getEdgeId().equals(edgeId)) {
					edgeEntity.setEdgeStatus(edgeStatus);
				}
			}
		}
	}

	/**
	 * saveFlow
	 * 
	 * @param flowEntity flowEntity
	 */
	public static void saveFlow(FlowEntity flowEntity) {
		String flowId = flowEntity.flowEntity.getFlowId();
		String historyId = flowEntity.flowEntity.getHistoryId();

		flowMap.put(getFlowKey(flowId, historyId), flowEntity);

	}

	/**
	 * getFlowEntityByKey
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return FlowEntity
	 */
	private static FlowEntity getFlowEntityByKey(String flowId, String historyId) {
		String flowKey = getFlowKey(flowId, historyId);
		return flowMap.get(flowKey);

	}

	/**
	 * getFlowKey
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return FlowKey
	 */
	private static String getFlowKey(String flowId, String historyId) {
		return flowId + "," + historyId;
	}
}
