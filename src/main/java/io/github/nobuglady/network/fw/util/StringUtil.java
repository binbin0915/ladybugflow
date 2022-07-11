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
package io.github.nobuglady.network.fw.util;

/**
 * 
 * @author NoBugLady
 *
 */
public class StringUtil {

	/**
	 * isEmpty
	 * 
	 * @param str str
	 * @return true:empty
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * isNotEmpty
	 * 
	 * @param str str
	 * @return true:not empty
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * padding
	 * 
	 * @param src        src
	 * @param paddingStr paddingStr
	 * @param length     length
	 * @return String
	 */
	public static String padding(long src, String paddingStr, int length) {
		int iSrc = (int) src;
		return String.format("%" + paddingStr + length + "d", iSrc);
	}
}
