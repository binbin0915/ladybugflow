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
package io.github.nobuglady.network.fw.component;

import java.util.List;

import io.github.nobuglady.network.fw.persistance.entity.FlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;

/**
 * 
 * @author NoBugLady
 *
 */
public interface IFlowAccessor {

	/**
	 * createHistoryId
	 * 
	 * @return HistoryId
	 */
	public String createHistoryId();

	/**
	 * saveFlow
	 * 
	 * @param flowEntity flowEntity
	 */
	public void saveFlow(FlowEntity flowEntity);

	/**
	 * updateFlowStatus
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param flowStatus flowStatus
	 */
	public void updateFlowStatus(String flowId, String historyId, int flowStatus);

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
			int nodeStatusDetail);

	/**
	 * updateNodeReturnValueByNodeId
	 * 
	 * @param flowId      flowId
	 * @param historyId   historyId
	 * @param nodeId      nodeId
	 * @param returnValue returnValue
	 */
	public void updateNodeReturnValueByNodeId(String flowId, String historyId, String nodeId, String returnValue);

	/**
	 * updateEdgeStatusByKey
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param edgeId     edgeId
	 * @param edgeStatus edgeStatus
	 */
	public void updateEdgeStatusByKey(String flowId, String historyId, String edgeId, int edgeStatus);

	/**
	 * updateNodeStatusByNodeId
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param nodeId     nodeId
	 * @param nodeStatus nodeStatus
	 */
	public void updateNodeStatusByNodeId(String flowId, String historyId, String nodeId, int nodeStatus);

	/**
	 * removeFlow
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 */
	public void removeFlow(String flowId, String historyId);

	/**
	 * removeAllComplete
	 */
	public void removeAllComplete();

	/**
	 * removeAllError
	 */
	public void removeAllError();

	/**
	 * selectFlowByKey
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryFlowEntity
	 */
	public HistoryFlowEntity selectFlowByKey(String flowId, String historyId);

	/**
	 * selectNodeByFlowHistoryId
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	public List<HistoryNodeEntity> selectNodeByFlowHistoryId(String flowId, String historyId);

	/**
	 * selectNodeByKey
	 * 
	 * @param flowId    flowId
	 * @param nodeId    nodeId
	 * @param historyId historyId
	 * @return HistoryNodeEntity
	 */
	public HistoryNodeEntity selectNodeByKey(String flowId, String nodeId, String historyId);

	/**
	 * selectNodeListByStatus
	 * 
	 * @param flowId     flowId
	 * @param historyId  historyId
	 * @param nodeStatus nodeStatus
	 * @return HistoryNodeEntity
	 */
	public List<HistoryNodeEntity> selectNodeListByStatus(String flowId, String historyId, int nodeStatus);

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
			int nodeStatusDetail);

	/**
	 * selectEdgeByFlowHistoryId
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return HistoryEdgeEntity
	 */
	public List<HistoryEdgeEntity> selectEdgeByFlowHistoryId(String flowId, String historyId);

	/**
	 * selectAll
	 * 
	 * @return HistoryFlowEntity
	 */
	public List<HistoryFlowEntity> selectAll();

}
