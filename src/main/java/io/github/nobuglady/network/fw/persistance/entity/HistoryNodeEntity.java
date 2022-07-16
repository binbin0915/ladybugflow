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
public class HistoryNodeEntity {

	private String flowId;
	private String nodeId;
	private String historyId;
	private String nodeName;
	private String refName;
	private int readyCheck;
	private int nodeType;
	private int startType;
	private int executeType;
	private String startCron;
	private String subFlowId;
	private String subNodeId;
	private String layoutX;
	private String layoutY;
	private int skipFlag;
	private String skipValue;
	private int nodeStatus;
	private int nodeStatusDetail;
	private String returnValue;
	private Date startTime;
	private Date finishTime;
	private String nodeResultMessage;
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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public int getReadyCheck() {
		return readyCheck;
	}

	public void setReadyCheck(int readyCheck) {
		this.readyCheck = readyCheck;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public int getStartType() {
		return startType;
	}

	public void setStartType(int startType) {
		this.startType = startType;
	}

	public int getExecuteType() {
		return executeType;
	}

	public void setExecuteType(int executeType) {
		this.executeType = executeType;
	}

	public String getStartCron() {
		return startCron;
	}

	public void setStartCron(String startCron) {
		this.startCron = startCron;
	}

	public String getSubFlowId() {
		return subFlowId;
	}

	public void setSubFlowId(String subFlowId) {
		this.subFlowId = subFlowId;
	}

	public String getSubNodeId() {
		return subNodeId;
	}

	public void setSubNodeId(String subNodeId) {
		this.subNodeId = subNodeId;
	}

	public String getLayoutX() {
		return layoutX;
	}

	public void setLayoutX(String layoutX) {
		this.layoutX = layoutX;
	}

	public String getLayoutY() {
		return layoutY;
	}

	public void setLayoutY(String layoutY) {
		this.layoutY = layoutY;
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

	public int getNodeStatus() {
		return nodeStatus;
	}

	public void setNodeStatus(int nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	public int getNodeStatusDetail() {
		return nodeStatusDetail;
	}

	public void setNodeStatusDetail(int nodeStatusDetail) {
		this.nodeStatusDetail = nodeStatusDetail;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getNodeResultMessage() {
		return nodeResultMessage;
	}

	public void setNodeResultMessage(String nodeResultMessage) {
		this.nodeResultMessage = nodeResultMessage;
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
