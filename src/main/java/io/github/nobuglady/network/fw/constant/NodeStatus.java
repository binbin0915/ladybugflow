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
package io.github.nobuglady.network.fw.constant;

/**
 * 
 * @author NoBugLady
 *
 */
public class NodeStatus {

	/** node condition skip */
	public static final int SKIPED = 999;

	/** node wait */
	public static final int WAIT = 0;
	/** node ready */
	public static final int READY = 2;

	/** node opened */
	public static final int OPENNING = 10;
	/** node running */
	public static final int RUNNING = 21;
	/** node complete */
	public static final int COMPLETE = 22;

	/** node closed */
	public static final int GO = 100;

}
