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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author NoBugLady
 *
 */
public class CompleteQueueManager {

	private static CompleteQueueManager instance = new CompleteQueueManager();

	private BlockingQueue<CompleteNodeResult> nodeCompleteQueue = new LinkedBlockingQueue<CompleteNodeResult>();

	private CompleteQueueManager() {

	}

	/**
	 * 
	 * @return
	 */
	public static CompleteQueueManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public CompleteNodeResult takeCompleteNode() throws InterruptedException {
		return nodeCompleteQueue.take();
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeId
	 */
	public void putCompleteNode(String flowId, String historyId, String nodeId) {
		try {
			nodeCompleteQueue.put(new CompleteNodeResult(flowId, historyId, nodeId));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
