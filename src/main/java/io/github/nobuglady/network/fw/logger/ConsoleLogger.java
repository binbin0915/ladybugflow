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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ConsoleLogger
 * 
 * @author NoBugLady
 *
 */
public class ConsoleLogger {

	public static volatile boolean debug_on = false;

	private PrintStream pw = System.out;

	private String key;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

	/**
	 * constructor
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 */
	private ConsoleLogger(String flowId, String historyId) {
		this.key = "[" + flowId + "][" + historyId + "]";
	}

	/**
	 * constructor
	 * 
	 * @param flowId flowId
	 */
	private ConsoleLogger(String flowId) {
		this.key = "[" + flowId + "]";
	}

	/**
	 * constructor
	 */
	private ConsoleLogger() {
		this.key = "";
	}

	/**
	 * getInstance
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @return ConsoleLogger
	 */
	public static ConsoleLogger getInstance(String flowId, String historyId) {
		return new ConsoleLogger(flowId, historyId);
	}

	/**
	 * getInstance
	 * 
	 * @param flowId flowId
	 * @return ConsoleLogger
	 */
	public static ConsoleLogger getInstance(String flowId) {
		return new ConsoleLogger(flowId);
	}

	/**
	 * getInstance
	 * 
	 * @return ConsoleLogger
	 */
	public static ConsoleLogger getInstance() {
		return new ConsoleLogger();
	}

	/**
	 * debug
	 * 
	 * @param message message
	 */
	public void debug(String message) {
		if (debug_on) {
			pw.println("[D]" + getHeader() + message);
		}
	}

	/**
	 * info
	 * 
	 * @param message message
	 */
	public void info(String message) {
		pw.println("[I]" + getHeader() + message);
	}

	/**
	 * error
	 * 
	 * @param message message
	 * @param e       exception
	 */
	public void error(String message, Throwable e) {
		pw.println("[E]" + getHeader() + message);
		pw.println("[E]" + getHeader() + e.getMessage());
	}

	/**
	 * getHeader
	 * 
	 * @return String
	 */
	private String getHeader() {
		return getTime() + " " + key + " " + Thread.currentThread().getName() + ":";
	}

	/**
	 * getTime
	 * 
	 * @return String
	 */
	private String getTime() {
		return sdf.format(new Date());
	}

}
