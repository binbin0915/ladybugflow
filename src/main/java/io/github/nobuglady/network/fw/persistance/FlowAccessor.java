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

import io.github.nobuglady.network.fw.component.IFlowAccessor;
import io.github.nobuglady.network.fw.constant.FlowStatus;
import io.github.nobuglady.network.fw.persistance.entity.FlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowAccessor implements IFlowAccessor {

	/** key: flowId, historyId */
	public static Map<String, FlowEntity> flowMap = new HashMap<>();

	/**
	 * createHistoryId
	 * 
	 * @return HistoryId
	 */
	public String createHistoryId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * selectFlowByKey
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryFlowEntity
	 */
	public HistoryFlowEntity selectFlowByKey(String flowId, String historyId) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			return flowEntity.flowEntity;
		} else {
			return null;
		}
	}

	/**
	 * selectNodeByFlowHistoryId
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	public List<HistoryNodeEntity> selectNodeByFlowHistoryId(String flowId, String historyId) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			return flowEntity.nodeEntityList;
		} else {
			return new ArrayList<>();
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
	public HistoryNodeEntity selectNodeByKey(String flowId, String nodeId, String historyId) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

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
	public void updateNodeStatusByNodeId(String flowId, String historyId, String nodeId, int nodeStatus) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					nodeEntity.setNodeStatus(nodeStatus);
				}
			}
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
	public List<HistoryNodeEntity> selectNodeListByStatus(String flowId, String historyId, int nodeStatus) {
		List<HistoryNodeEntity> resultList = new ArrayList<>();

		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

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
	public List<HistoryNodeEntity> selectNodeListByStatusDetail(String flowId, String historyId, int nodeStatus,
			int nodeStatusDetail) {
		List<HistoryNodeEntity> resultList = new ArrayList<>();

		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

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
	 * selectEdgeByFlowHistoryId
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryEdgeEntity
	 */
	public List<HistoryEdgeEntity> selectEdgeByFlowHistoryId(String flowId, String historyId) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			return flowEntity.edgeEntityList;
		} else {
			return new ArrayList<>();
		}

	}

	/**
	 * updateFlowStatus
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param flowStatus flowStatus
	 */
	public void updateFlowStatus(String flowId, String historyId, int flowStatus) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

		if (flowEntity != null) {
			flowEntity.flowEntity.setFlowStatus(flowStatus);
		}
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
	public void updateNodeStatusDetailByNodeId(String flowId, String historyId, String nodeId, int nodeStatus,
			int nodeStatusDetail) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

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
	public void updateNodeReturnValueByNodeId(String flowId, String historyId, String nodeId, String returnValue) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

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
	public void updateEdgeStatusByKey(String flowId, String historyId, String edgeId, int edgeStatus) {
		FlowEntity flowEntity = getFlowEntityByKey(flowId, historyId);

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
	public void saveFlow(FlowEntity flowEntity) {
		String flowId = flowEntity.flowEntity.getFlowId();
		String historyId = flowEntity.flowEntity.getHistoryId();

		flowMap.put(getFlowKey(flowId, historyId), flowEntity);

	}

	/**
	 * removeFlow
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 */
	public void removeFlow(String flowId, String historyId) {
		flowMap.remove(getFlowKey(flowId, historyId));
	}

	/**
	 * removeAllComplete
	 * 
	 */
	public void removeAllComplete() {

		List<String> keys = new ArrayList<>();
		for (Map.Entry<String, FlowEntity> entry : flowMap.entrySet()) {
			if (entry.getValue().flowEntity.getFlowStatus() == FlowStatus.COMPLETE) {
				keys.add(entry.getKey());
			}
		}

		for (String key : keys) {
			flowMap.remove(key);
		}
	}

	/**
	 * removeAllError
	 */
	public void removeAllError() {

		List<String> keys = new ArrayList<>();
		for (Map.Entry<String, FlowEntity> entry : flowMap.entrySet()) {
			if (entry.getValue().flowEntity.getFlowStatus() == FlowStatus.ERROR) {
				keys.add(entry.getKey());
			}
		}

		for (String key : keys) {
			flowMap.remove(key);
		}
	}

	/**
	 * selectAll
	 * 
	 * @return HistoryFlowEntity
	 */
	public List<HistoryFlowEntity> selectAll() {
		List<HistoryFlowEntity> entityList = new ArrayList<>();

		for (Map.Entry<String, FlowEntity> entry : flowMap.entrySet()) {
			entityList.add(entry.getValue().flowEntity);
		}

		return entityList;
	}

	/**
	 * getFlowEntityByKey
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return FlowEntity
	 */
	private FlowEntity getFlowEntityByKey(String flowId, String historyId) {
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
	private String getFlowKey(String flowId, String historyId) {
		return flowId + "," + historyId;
	}

}
