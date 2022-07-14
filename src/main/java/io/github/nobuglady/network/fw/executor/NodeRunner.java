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

import java.util.concurrent.CancellationException;

import io.github.nobuglady.network.fw.FlowRunner;
import io.github.nobuglady.network.fw.constant.NodeStatusDetail;
import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.persistance.FlowContainer;
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
	 * Constructor
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @param nodeId    nodeId
	 */
	public NodeRunner(String flowId, String historyId, String nodeId) {

		this.flowId = flowId;
		this.historyId = historyId;
		this.nodeId = nodeId;
	}

	/**
	 * run
	 */
	public void run() {

		String nodeName = "";

		// start log
		ConsoleLogger consoleLogger = ConsoleLogger.getInstance(flowId, historyId);

		consoleLogger.debug("-------------------------------------------------------------------");
		consoleLogger.debug("[FLOW]" + flowId + ",[HISTORY]" + historyId + ",[NODE]" + nodeId + "");
		consoleLogger.debug("-------------------------------------------------------------------");

		try {

			HistoryNodeEntity historyNodeEntity = FlowContainer.selectNodeByKey(flowId, nodeId, historyId);
			nodeName = historyNodeEntity.getNodeName();

			// run
			FlowRunner flowRunner = FlowContainer.flowRunnerMap.get(flowId + "," + historyId);
			consoleLogger.debug(" [NODE RUNNING]" + historyNodeEntity.getNodeName());
			int returnValue = flowRunner.execute(historyNodeEntity.getFlowId(), historyNodeEntity.getNodeId(),
					historyNodeEntity.getHistoryId(), historyNodeEntity);

			// complete
			consoleLogger.debug(" [NODE COMPLETE][" + returnValue + "]" + historyNodeEntity.getNodeName());

			CompleteQueueManager.getInstance().putCompleteNode(flowId, historyId, nodeId,
					NodeStatusDetail.COMPLETE_SUCCESS, returnValue);

		} catch (CancellationException e) {

			consoleLogger.error(" [NODE CANCEL]" + nodeName, e);
			CompleteQueueManager.getInstance().putCompleteNode(flowId, historyId, nodeId,
					NodeStatusDetail.COMPLETE_CANCEL, null);

		} catch (Throwable e) {

			e.printStackTrace();

			consoleLogger.error(" [NODE ERROR]" + nodeName, e);
			CompleteQueueManager.getInstance().putCompleteNode(flowId, historyId, nodeId,
					NodeStatusDetail.COMPLETE_ERROR, null);

		} finally {

		}

	}

}
