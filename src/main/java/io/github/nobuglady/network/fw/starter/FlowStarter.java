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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.github.nobuglady.network.fw.FlowRunner;
import io.github.nobuglady.network.fw.component.FlowComponentFactory;
import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.queue.complete.CompleteNodeResult;
import io.github.nobuglady.network.fw.queue.ready.ReadyNodeResult;
import io.github.nobuglady.network.fw.util.StringUtil;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowStarter {

	public static boolean nodeExecutorRemote = false;
	public static boolean deleteOnComplete = true;
	public static boolean deleteOnError = true;

	public static BlockingQueue<ReadyNodeResult> nodeReadyQueue = new LinkedBlockingQueue<ReadyNodeResult>();
	public static BlockingQueue<CompleteNodeResult> nodeCompleteQueue = new LinkedBlockingQueue<CompleteNodeResult>();

	/** key: flowId, historyId */
	public static Map<String, FlowRunner> flowRunnerMap = new HashMap<>();

	private static ConsoleLogger logger = ConsoleLogger.getInstance();

	static {

		try {
			Class.forName(FlowComponentFactory.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		nodeExecutorRemote = false;

		Properties prop = new Properties();
		try {
			prop.load(FlowStarter.class.getClassLoader().getResourceAsStream("ladybugflow.properties"));
		} catch (IOException | NullPointerException e) {
			logger.info("ladybugflow.properties in root path not found, use default configuration");
		}

		if (prop != null) {
			String nodeExecutorRemoteStr = prop.getProperty("node.executor.remote");
			String deleteOnCompleteStr = prop.getProperty("flow.delete_on_complete");
			String deleteOnErrorStr = prop.getProperty("flow.delete_on_error");

			if (StringUtil.isNotEmpty(nodeExecutorRemoteStr)) {
				nodeExecutorRemote = Boolean.valueOf(nodeExecutorRemoteStr);
			}
			if (StringUtil.isNotEmpty(deleteOnCompleteStr)) {
				deleteOnComplete = Boolean.valueOf(deleteOnCompleteStr);
			}
			if (StringUtil.isNotEmpty(deleteOnErrorStr)) {
				deleteOnError = Boolean.valueOf(deleteOnErrorStr);
			}
		}

		if (!nodeExecutorRemote) {
			FlowComponentFactory.getReadyQueueReceiver().startConsumerThread();
			logger.info("Ready queue consumer thread started.");
		}

		FlowComponentFactory.getCompleteQueueReceiver().startConsumerThread();
		logger.info("Complete queue consumer thread started.");

	}

	public static void shutdown() {
		if (!nodeExecutorRemote) {
			FlowComponentFactory.getReadyQueueReceiver().shutdown();
			logger.info("Ready queue thread stoped.");
		}

		FlowComponentFactory.getCompleteQueueReceiver().shutdown();
		logger.info("Ready queue thread stoped.");

		if (FlowComponentFactory.getNodeExecutor() != null) {
			FlowComponentFactory.getNodeExecutor().shutdown();
			logger.info("NodePool stoped.");
		}
	}
}
