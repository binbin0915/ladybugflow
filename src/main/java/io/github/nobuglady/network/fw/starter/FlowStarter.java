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
package io.github.nobuglady.network.fw.starter;

import io.github.nobuglady.network.fw.executor.NodePool;
import io.github.nobuglady.network.fw.executor.ReadyQueueConsumerThread;
import io.github.nobuglady.network.fw.queue.CompleteQueueConsumerThread;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowStarter {

	private static ReadyQueueConsumerThread readyQueueConsumerThread;
	private static CompleteQueueConsumerThread completeQueueConsumerThread;
	
	static {
		readyQueueConsumerThread = new ReadyQueueConsumerThread(new NodePool());
		readyQueueConsumerThread.start();
		System.out.println("Ready queue thread started.");

		completeQueueConsumerThread = new CompleteQueueConsumerThread();
		completeQueueConsumerThread.start();
		System.out.println("Complete queue thread started.");
	}
	
	public static void shutdown() {
		readyQueueConsumerThread.shutdown();
		completeQueueConsumerThread.shutdown();
		NodePool.nodePool.shutdown();
	}
}
