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
package io.github.nobuglady.network.fw.component;

import java.io.IOException;
import java.util.Properties;

import io.github.nobuglady.network.fw.starter.FlowStarter;
import io.github.nobuglady.network.fw.util.StringUtil;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowComponentFactory {

	private static IReadyQueueReceiver readyQueueReceiver;
	private static IReadyQueueSender readyQueueSender;
	private static ICompleteQueueReceiver completeQueueReceiver;
	private static ICompleteQueueSender completeQueueSender;
	private static INodeExecutor nodeExecutor;
	private static IFlowMarker flowMarker;
	private static IFlowAccessor flowAccessor;

	static {

		String readyQueueReceiverClassName = "io.github.nobuglady.network.fw.queue.ready.ReadyQueueReceiver";
		String readyQueueSenderClassName = "io.github.nobuglady.network.fw.queue.ready.ReadyQueueSender";
		String completeQueueReceiverClassName = "io.github.nobuglady.network.fw.queue.complete.CompleteQueueReceiver";
		String completeQueueSenderClassName = "io.github.nobuglady.network.fw.queue.complete.CompleteQueueSender";
		String nodeExecutorClassName = "io.github.nobuglady.network.fw.executor.NodePool";
		String flowMarkerClassName = "io.github.nobuglady.network.fw.marker.FlowMarker";
		String flowAccessorClassName = "io.github.nobuglady.network.fw.persistance.FlowAccessor";

		Properties prop = new Properties();
		try {
			prop.load(FlowStarter.class.getClassLoader().getResourceAsStream("ladybugflow.properties"));
		} catch (IOException | NullPointerException e) {
		}

		if (prop != null) {
			String readyQueueReceiverClassNameStr = prop.getProperty("queue.ready.receiver");
			String readyQueueSenderClassNameStr = prop.getProperty("queue.ready.sender");
			String completeQueueReceiverClassNameStr = prop.getProperty("queue.complete.receiver");
			String completeQueueSenderClassNameStr = prop.getProperty("queue.complete.sender");
			String nodeExecutorClassNameStr = prop.getProperty("node.executor");
			String flowMarkerClassNameStr = prop.getProperty("flow.marker");
			String flowAccessorClassNameStr = prop.getProperty("flow.accessor");

			if (StringUtil.isNotEmpty(readyQueueReceiverClassNameStr)) {
				readyQueueReceiverClassName = readyQueueReceiverClassNameStr;
			}
			if (StringUtil.isNotEmpty(readyQueueSenderClassNameStr)) {
				readyQueueSenderClassName = readyQueueSenderClassNameStr;
			}
			if (StringUtil.isNotEmpty(completeQueueReceiverClassNameStr)) {
				completeQueueReceiverClassName = completeQueueReceiverClassNameStr;
			}
			if (StringUtil.isNotEmpty(completeQueueSenderClassNameStr)) {
				completeQueueSenderClassName = completeQueueSenderClassNameStr;
			}
			if (StringUtil.isNotEmpty(nodeExecutorClassNameStr)) {
				nodeExecutorClassName = nodeExecutorClassNameStr;
			}
			if (StringUtil.isNotEmpty(flowMarkerClassNameStr)) {
				flowMarkerClassName = flowMarkerClassNameStr;
			}
			if (StringUtil.isNotEmpty(flowAccessorClassNameStr)) {
				flowAccessorClassName = flowAccessorClassNameStr;
			}

		}

		try {

			flowAccessor = (IFlowAccessor) Class.forName(flowAccessorClassName).newInstance();
			nodeExecutor = (INodeExecutor) Class.forName(nodeExecutorClassName).newInstance();
			flowMarker = (IFlowMarker) Class.forName(flowMarkerClassName).newInstance();

			readyQueueReceiver = (IReadyQueueReceiver) Class.forName(readyQueueReceiverClassName).newInstance();
			readyQueueSender = (IReadyQueueSender) Class.forName(readyQueueSenderClassName).newInstance();
			completeQueueReceiver = (ICompleteQueueReceiver) Class.forName(completeQueueReceiverClassName)
					.newInstance();
			completeQueueSender = (ICompleteQueueSender) Class.forName(completeQueueSenderClassName).newInstance();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * getReadyQueueSender
	 * 
	 * @return ReadyQueueSender
	 */
	public static IReadyQueueSender getReadyQueueSender() {
		return readyQueueSender;
	}

	/**
	 * getReadyQueueReceiver
	 * 
	 * @return ReadyQueueReceiver
	 */
	public static IReadyQueueReceiver getReadyQueueReceiver() {
		return readyQueueReceiver;
	}

	/**
	 * getCompleteQueueSender
	 * 
	 * @return CompleteQueueSender
	 */
	public static ICompleteQueueSender getCompleteQueueSender() {
		return completeQueueSender;
	}

	/**
	 * getCompleteQueueReceiver
	 * 
	 * @return CompleteQueueReceiver
	 */
	public static ICompleteQueueReceiver getCompleteQueueReceiver() {
		return completeQueueReceiver;
	}

	/**
	 * getNodeExecutor
	 * 
	 * @return NodeExecutor
	 */
	public static INodeExecutor getNodeExecutor() {
		return nodeExecutor;
	}

	/**
	 * getFlowMarker
	 * 
	 * @return FlowMarker
	 */
	public static IFlowMarker getFlowMarker() {
		return flowMarker;
	}

	/**
	 * getFlowAccessor
	 * 
	 * @return FlowAccessor
	 */
	public static IFlowAccessor getFlowAccessor() {
		return flowAccessor;
	}

}
