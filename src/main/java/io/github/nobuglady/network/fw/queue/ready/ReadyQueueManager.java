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
package io.github.nobuglady.network.fw.queue.ready;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author NoBugLady
 *
 */
public class ReadyQueueManager {

	private static ReadyQueueManager instance = new ReadyQueueManager();

	private BlockingQueue<ReadyNodeResult> nodeCompleteQueue = new LinkedBlockingQueue<ReadyNodeResult>();

	private ReadyQueueManager() {

	}

	/**
	 * getInstance
	 * 
	 * @return ReadyQueueManager
	 */
	public static ReadyQueueManager getInstance() {
		return instance;
	}

	/**
	 * takeCompleteNode
	 * 
	 * @return ReadyNodeResult
	 * @throws InterruptedException InterruptedException
	 */
	public ReadyNodeResult takeCompleteNode() throws InterruptedException {
		return nodeCompleteQueue.take();
	}

	/**
	 * putReadyNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @param nodeId    nodeId
	 */
	public void putReadyNode(String flowId, String historyId, String nodeId) {
		try {
			nodeCompleteQueue.put(new ReadyNodeResult(flowId, historyId, nodeId));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
