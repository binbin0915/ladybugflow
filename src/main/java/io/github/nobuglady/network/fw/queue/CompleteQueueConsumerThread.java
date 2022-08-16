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

import io.github.nobuglady.network.fw.component.FlowComponentFactory;
import io.github.nobuglady.network.fw.queue.complete.CompleteNodeResult;
import io.github.nobuglady.network.fw.starter.FlowStarter;

/**
 * 
 * @author NoBugLady
 *
 */
public class CompleteQueueConsumerThread extends Thread {

	private volatile boolean stopFlag = false;

	/**
	 * Constructor
	 * 
	 */
	public CompleteQueueConsumerThread() {
	}

	/**
	 * run
	 */
	public void run() {

		while (!this.stopFlag) {
			try {
				CompleteNodeResult nodeResult = FlowStarter.nodeCompleteQueue.take();
				if (nodeResult != null) {
					FlowComponentFactory.getFlowMarker().onNodeComplete(nodeResult);
				} else {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				if (!this.stopFlag) {
					e.printStackTrace();
					break;
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
