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
public class NodeStatusDetail {

	/** node execute success */
	public static final int NONE = 0;
	/** node execute success */
	public static final int COMPLETE_SUCCESS = 1;
	/** node execute error */
	public static final int COMPLETE_ERROR = -1;
	/** node execute timeout */
	public static final int COMPLETE_TIMEOUT = -2;
	/** node cancel */
	public static final int COMPLETE_CANCEL = -3;

}
