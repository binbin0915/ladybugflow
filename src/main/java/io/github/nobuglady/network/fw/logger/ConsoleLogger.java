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
package io.github.nobuglady.network.fw.logger;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ConsoleLogger
 * 
 * @author NoBugLady
 *
 */
public class ConsoleLogger {

	public static boolean enabled = false;

	private static Map<String, ConsoleLogger> instanceMap = new HashMap<>();

	private PrintStream pw = System.out;

	/**
	 * constructor
	 */
	private ConsoleLogger(String flowId, String historyId) {

	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	public static synchronized ConsoleLogger getInstance(String flowId, String historyId) {
		String instanceId = flowId + "," + historyId;
		ConsoleLogger instance = instanceMap.get(instanceId);
		if (instance == null) {
			instance = new ConsoleLogger(flowId, historyId);
			instanceMap.put(instanceId, instance);
		}
		return instance;
	}

	/**
	 * 
	 * @param message
	 */
	public void info(String message) {
		if (enabled) {
			pw.println(Thread.currentThread().getName() + ":" + message);
		}
	}

	/**
	 * 
	 * @param message
	 * @param e
	 */
	public void error(String message, Throwable e) {
		if (enabled) {
			pw.println(Thread.currentThread().getName() + ":" + message);
			pw.println(Thread.currentThread().getName() + ":" + e.getMessage());
		}
	}

}
