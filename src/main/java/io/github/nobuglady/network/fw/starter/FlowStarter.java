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

import java.io.IOException;
import java.util.Properties;

import io.github.nobuglady.network.fw.FlowManager;
import io.github.nobuglady.network.fw.executor.INodeExecutor;
import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.queue.complete.ICompleteQueue;
import io.github.nobuglady.network.fw.queue.ready.IReadyQueue;
import io.github.nobuglady.network.fw.util.StringUtil;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowStarter {

	public static IReadyQueue readyQueue;
	public static ICompleteQueue completeQueue;
	public static INodeExecutor nodeExecutor;
	public static boolean nodeExecutorRemote = false;

	private static ConsoleLogger logger = ConsoleLogger.getInstance();

	static {

		String readyQueueClassName = "io.github.nobuglady.network.fw.queue.ready.ReadyQueueManager";
		String completeQueueClassName = "io.github.nobuglady.network.fw.queue.complete.CompleteQueueManager";
		String nodeExecutorClassName = "io.github.nobuglady.network.fw.executor.NodePool";

		nodeExecutorRemote = false;

		Properties prop = new Properties();
		try {
			prop.load(FlowStarter.class.getClassLoader().getResourceAsStream("ladybugflow.properties"));
		} catch (IOException | NullPointerException e) {
			logger.info("ladybugflow.properties in root path not found, use default configuration");
		}

		if (prop != null) {
			String readyQueueClassNameStr = prop.getProperty("queue.ready.manager");
			String completeQueueClassNameStr = prop.getProperty("queue.complete.manager");
			String nodeExecutorClassNameStr = prop.getProperty("node.executor");
			String nodeExecutorRemoteStr = prop.getProperty("node.executor.remote");

			if (StringUtil.isNotEmpty(readyQueueClassNameStr)) {
				readyQueueClassName = readyQueueClassNameStr;
			}
			if (StringUtil.isNotEmpty(completeQueueClassNameStr)) {
				completeQueueClassName = completeQueueClassNameStr;
			}
			if (StringUtil.isNotEmpty(nodeExecutorClassNameStr)) {
				nodeExecutorClassName = nodeExecutorClassNameStr;
			}
			if (StringUtil.isNotEmpty(nodeExecutorRemoteStr)) {
				nodeExecutorRemote = Boolean.valueOf(nodeExecutorRemoteStr);
			}
		}

		try {

			readyQueue = (IReadyQueue) Class.forName(readyQueueClassName).newInstance();
			completeQueue = (ICompleteQueue) Class.forName(completeQueueClassName).newInstance();

			if (!nodeExecutorRemote) {
				nodeExecutor = (INodeExecutor) Class.forName(nodeExecutorClassName).newInstance();
				readyQueue.startConsumerThread(nodeExecutor);
				logger.info("Ready queue consumer thread started.");
			}

			completeQueue.startConsumerThread(new FlowManager());
			logger.info("Complete queue consumer thread started.");

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void shutdown() {
		if (!nodeExecutorRemote) {
			readyQueue.shutdown();
			logger.info("Ready queue thread stoped.");
		}

		completeQueue.shutdown();
		logger.info("Ready queue thread stoped.");

		if (nodeExecutor != null) {
			nodeExecutor.shutdown();
			logger.info("NodePool stoped.");
		}
	}
}
