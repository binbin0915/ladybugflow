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
package io.github.nobuglady.network.fw.marker.helper;

import java.util.ArrayList;
import java.util.List;

import io.github.nobuglady.network.fw.marker.helper.model.FlowHelperModel;
import io.github.nobuglady.network.fw.persistance.FlowContainer;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowHelper {

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	public FlowHelperModel getFlow(String flowId, String historyId) {

		List<HistoryEdgeEntity> edgeHistoryEntityList = FlowContainer.selectEdgeByFlowHistoryId(flowId, historyId);
		List<HistoryNodeEntity> nodeHistoryEntityList = FlowContainer.selectNodeByFlowHistoryId(flowId, historyId);
		HistoryFlowEntity flowHistoryEntity = FlowContainer.selectFlowByKey(flowId, historyId);

		return loadConfig(flowHistoryEntity, edgeHistoryEntityList, nodeHistoryEntityList);
	}

	/**
	 * 
	 * @param historyFlowEntity
	 * @param historyEdgeEntityList
	 * @param historyNodeEntityList
	 * @return
	 */
	private static FlowHelperModel loadConfig(HistoryFlowEntity historyFlowEntity, List<HistoryEdgeEntity> historyEdgeEntityList,
			List<HistoryNodeEntity> historyNodeEntityList) {

		FlowHelperModel flow = new FlowHelperModel();
		flow.setHistoryId(historyFlowEntity.getHistoryId());
		flow.setStatus(historyFlowEntity.getFlowStatus());

		/*
		 * make node map
		 */
		for (HistoryNodeEntity historyNodeEntity : historyNodeEntityList) {
			flow.getNodeMap().put(historyNodeEntity.getNodeId(), historyNodeEntity);
		}

		/*
		 * Initialize previous and next node
		 */
		for (HistoryEdgeEntity historyEdgeEntity : historyEdgeEntityList) {

			String from = historyEdgeEntity.getFromNodeId();
			String to = historyEdgeEntity.getToNodeId();

			List<HistoryEdgeEntity> existEdges = flow.getEdgesMap().get(from);

			if (existEdges == null) {
				existEdges = new ArrayList<HistoryEdgeEntity>();
				flow.getEdgesMap().put(from, existEdges);

			}

			existEdges.add(historyEdgeEntity);

			List<HistoryEdgeEntity> existBackEdges = flow.getEdgesBackMap().get(to);
			if (existBackEdges == null) {
				existBackEdges = new ArrayList<HistoryEdgeEntity>();
				flow.getEdgesBackMap().put(to, existBackEdges);
			}

			existBackEdges.add(historyEdgeEntity);

		}

		return flow;
	}
}
