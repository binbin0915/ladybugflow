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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.queue.ready.ReadyNodeResult;

/**
 * 
 * @author NoBugLady
 *
 */
public class NodePool {

	public static ExecutorService nodePool = Executors.newCachedThreadPool();

	private static ConsoleLogger logger = ConsoleLogger.getInstance();

	public NodePool() {
		logger.info("NodePool started.");
	}

	/**
	 * onNodeReady
	 * 
	 * @param nodeResult nodeResult
	 */
	public void onNodeReady(ReadyNodeResult nodeResult) {

		String flowId = nodeResult.getFlowId();
		String historyId = nodeResult.getHistoryId();
		String nodeId = nodeResult.getNodeId();

		NodeRunner nodeRunner = new NodeRunner(flowId, historyId, nodeId);

		nodePool.submit(nodeRunner);
	}
}
