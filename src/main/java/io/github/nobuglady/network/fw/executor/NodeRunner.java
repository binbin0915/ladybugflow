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
package io.github.nobuglady.network.fw.executor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CancellationException;

import io.github.nobuglady.network.fw.FlowRunner;
import io.github.nobuglady.network.fw.constant.NodeStatus;
import io.github.nobuglady.network.fw.constant.NodeStatusDetail;
import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.persistance.FlowContainer;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;
import io.github.nobuglady.network.fw.queue.complete.CompleteQueueManager;

/**
 * 
 * @author NoBugLady
 *
 */
public class NodeRunner implements Runnable {

	private String flowId;
	private String historyId;
	private String nodeId;

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeId
	 * @param historyFlowDao
	 * @param historyNodeDao
	 * @param historyNodeHttpDao
	 * @param historyNodeShellDao
	 * @param nodeDelegator
	 */
	public NodeRunner(String flowId, String historyId, String nodeId) {

		this.flowId = flowId;
		this.historyId = historyId;
		this.nodeId = nodeId;
	}

	/**
	 * 
	 */
	public void run() {

		String nodeName = "";
		
		// start log
		ConsoleLogger consoleLogger = ConsoleLogger.getInstance(flowId, historyId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

		consoleLogger.info("-------------------------------------------------------------------");
		consoleLogger.info("[FLOW]" + flowId + ",[HISTORY]" + historyId + ",[NODE]" + nodeId + "");
		consoleLogger.info("-------------------------------------------------------------------");
		
		try {

			// get flow, node
			HistoryFlowEntity historyFlowEntity = FlowContainer.selectFlowByKey(flowId, historyId);
			String flowId = historyFlowEntity.getFlowId();

			HistoryNodeEntity historyNodeEntity = FlowContainer.selectNodeByKey(flowId, nodeId, historyId);
			nodeName = historyNodeEntity.getNodeName();

			// check ready
			if (NodeStatus.READY != historyNodeEntity.getNodeStatus()) {
				consoleLogger.info(sdf.format(new Date()) + " [NOT READY]" + historyNodeEntity.getNodeStatus());
				return;
			}

			// update to running
			FlowContainer.updateNodeStatusByNodeId(flowId, historyId, nodeId, NodeStatus.RUNNING);

			// run
			FlowRunner flowRunner = FlowContainer.flowRunnerMap.get(flowId + "," + historyId);
			consoleLogger.info(sdf.format(new Date()) + " [NODE RUNNING]" + historyNodeEntity.getNodeName());
			int returnValue = flowRunner.execute(historyNodeEntity.getFlowId(), historyNodeEntity.getNodeId(), historyNodeEntity.getHistoryId(), historyNodeEntity);

			// complete
			consoleLogger.info(
					sdf.format(new Date()) + " [NODE COMPLETE][" + returnValue + "]" + historyNodeEntity.getNodeName());
			FlowContainer.updateNodeStatusDetailByNodeId(flowId, historyId, nodeId, NodeStatus.COMPLETE,
					NodeStatusDetail.COMPLETE_SUCCESS);
			
			CompleteQueueManager.getInstance().putCompleteNode(flowId, historyId, nodeId);

		} catch (CancellationException e) {

			consoleLogger.error(sdf.format(new Date()) + " [NODE CANCEL]" + nodeName, e);
			FlowContainer.updateNodeStatusDetailByNodeId(flowId, historyId, nodeId, NodeStatus.COMPLETE,
					NodeStatusDetail.COMPLETE_CANCEL);
			CompleteQueueManager.getInstance().putCompleteNode(flowId, historyId, nodeId);

		} catch (Throwable e) {
			
			e.printStackTrace();
			
			consoleLogger.error(sdf.format(new Date()) + " [NODE ERROR]" + nodeName, e);
			FlowContainer.updateNodeStatusDetailByNodeId(flowId, historyId, nodeId, NodeStatus.COMPLETE,
					NodeStatusDetail.COMPLETE_ERROR);
			CompleteQueueManager.getInstance().putCompleteNode(flowId, historyId, nodeId);

		} finally {

		}

	}

}
