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
public class FlowStatus {
	/** READY */
	public static final int READY = 0;
	/** PROCESSING */
	public static final int PROCESSING = 1;
	/** COMPLETE */
	public static final int COMPLETE = 2;
	/** ERROR */
	public static final int ERROR = -1;
	/** CANCEL */
	public static final int CANCEL = -2;

	/**
	 * getFlowStatusString
	 * 
	 * @param status status
	 * @return status string
	 */
	public static String getFlowStatusString(int status) {
		switch (status) {
		case READY:
			return "READY";
		case PROCESSING:
			return "PROCESSING";
		case COMPLETE:
			return "COMPLETE";
		case ERROR:
			return "ERROR";
		case CANCEL:
			return "CANCEL";
		default:
			return "UNKNOWN";
		}
	}
}
