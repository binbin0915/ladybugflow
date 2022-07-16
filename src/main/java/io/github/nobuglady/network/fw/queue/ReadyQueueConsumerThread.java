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
package io.github.nobuglady.network.fw.queue;

import io.github.nobuglady.network.fw.executor.NodePool;
import io.github.nobuglady.network.fw.queue.ready.IReadyQueue;
import io.github.nobuglady.network.fw.queue.ready.ReadyNodeResult;

/**
 * 
 * @author NoBugLady
 *
 */
public class ReadyQueueConsumerThread extends Thread {

	private volatile boolean stopFlag = false;

	private NodePool nodePool;

	private IReadyQueue readyQueue;

	/**
	 * Constructor
	 * 
	 * @param readyQueue readyQueue
	 * @param nodePool   nodePool
	 */
	public ReadyQueueConsumerThread(IReadyQueue readyQueue, NodePool nodePool) {
		this.readyQueue = readyQueue;
		this.nodePool = nodePool;
	}

	/**
	 * run
	 */
	public void run() {

		while (!this.stopFlag) {
			try {
				ReadyNodeResult nodeResult = readyQueue.takeCompleteNode();
				if (nodeResult != null) {
					nodePool.onNodeReady(nodeResult);
				} else {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				if (!this.stopFlag) {
					e.printStackTrace();
				} else {
					break;
				}
			}
		}
	}

	/**
	 * shutdown
	 */
	public void shutdown() {

		this.stopFlag = true;
		this.interrupt();
	}

}
