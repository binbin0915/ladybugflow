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
package io.github.nobuglady.network.fw.persistance.entity;

import java.util.Date;

/**
 * 
 * @author NoBugLady
 *
 */
public class HistoryEdgeEntity {

	private String flowId;
	private String edgeId;
	private String historyId;
	private String fromNodeId;
	private String toNodeId;
	private String edgeCondition;
	private int edgeExceptionReturnType;
	private int skipFlag;
	private String skipValue;
	private int edgeStatus;
	private int disableFlag;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(String edgeId) {
		this.edgeId = edgeId;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getFromNodeId() {
		return fromNodeId;
	}

	public void setFromNodeId(String fromNodeId) {
		this.fromNodeId = fromNodeId;
	}

	public String getToNodeId() {
		return toNodeId;
	}

	public void setToNodeId(String toNodeId) {
		this.toNodeId = toNodeId;
	}

	public String getEdgeCondition() {
		return edgeCondition;
	}

	public void setEdgeCondition(String edgeCondition) {
		this.edgeCondition = edgeCondition;
	}

	public int getEdgeExceptionReturnType() {
		return edgeExceptionReturnType;
	}

	public void setEdgeExceptionReturnType(int edgeExceptionReturnType) {
		this.edgeExceptionReturnType = edgeExceptionReturnType;
	}

	public int getSkipFlag() {
		return skipFlag;
	}

	public void setSkipFlag(int skipFlag) {
		this.skipFlag = skipFlag;
	}

	public String getSkipValue() {
		return skipValue;
	}

	public void setSkipValue(String skipValue) {
		this.skipValue = skipValue;
	}

	public int getEdgeStatus() {
		return edgeStatus;
	}

	public void setEdgeStatus(int edgeStatus) {
		this.edgeStatus = edgeStatus;
	}

	public int getDisableFlag() {
		return disableFlag;
	}

	public void setDisableFlag(int disableFlag) {
		this.disableFlag = disableFlag;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
