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
package io.github.nobuglady.network.fw.marker.helper.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowHelperModel {

	private String flowId;
	private String historyId;
	private int status;

	private Map<String, List<HistoryEdgeEntity>> edgesMap = new HashMap<String, List<HistoryEdgeEntity>>();
	private Map<String, List<HistoryEdgeEntity>> edgesBackMap = new HashMap<String, List<HistoryEdgeEntity>>();
	private Map<String, HistoryNodeEntity> nodeMap = new HashMap<String, HistoryNodeEntity>();

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, List<HistoryEdgeEntity>> getEdgesMap() {
		return edgesMap;
	}

	public void setEdgesMap(Map<String, List<HistoryEdgeEntity>> edgesMap) {
		this.edgesMap = edgesMap;
	}

	public Map<String, List<HistoryEdgeEntity>> getEdgesBackMap() {
		return edgesBackMap;
	}

	public void setEdgesBackMap(Map<String, List<HistoryEdgeEntity>> edgesBackMap) {
		this.edgesBackMap = edgesBackMap;
	}

	public Map<String, HistoryNodeEntity> getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(Map<String, HistoryNodeEntity> nodeMap) {
		this.nodeMap = nodeMap;
	}

}
