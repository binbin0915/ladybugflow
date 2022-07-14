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
package io.github.nobuglady.network.fw.queue.complete;

/**
 * 
 * @author NoBugLady
 *
 */
public class CompleteNodeResult {

	private String flowId;
	private String historyId;
	private String nodeId;
	private int nodeStatus;
	private Integer nodeResult;

	public CompleteNodeResult() {

	}

	public CompleteNodeResult(String flowId, String historyId, String nodeId, int nodeStatus, Integer nodeResult) {
		this.flowId = flowId;
		this.historyId = historyId;
		this.nodeId = nodeId;
		this.nodeStatus = nodeStatus;
		this.nodeResult = nodeResult;
	}

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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public int getNodeStatus() {
		return nodeStatus;
	}

	public void setNodeStatus(int nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	public Integer getNodeResult() {
		return nodeResult;
	}

	public void setNodeResult(Integer nodeResult) {
		this.nodeResult = nodeResult;
	}

}
