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
package io.github.nobuglady.network.fw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.nobuglady.network.fw.annotation.Node;
import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;
import io.github.nobuglady.network.fw.starter.FlowStarter;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowRunner {

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeId
	 * @return
	 */
	public int execute(String flowId, String nodeId, String historyId, HistoryNodeEntity nodeEntity) throws Exception {

		ConsoleLogger logger = ConsoleLogger.getInstance(flowId, historyId);

		String nodeName = nodeEntity.getNodeName();
		logger.info("execute node id:" + nodeId);
		logger.info("execute node name:" + nodeEntity.getNodeName());

		Method methods[] = this.getClass().getMethods();
		if (methods != null) {
			for (Method method : methods) {
				Node node = method.getAnnotation(Node.class);
				if (node != null) {
					if (node.id().equals(nodeId) || node.label().equals(nodeName)) {
						try {
							method.invoke(this);
							return 0;
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
							throw e;
						}
					}
				}
			}
		}
		return 0;

	}

	/**
	 * 
	 * @return
	 */
	public int startFlow() {

		try {
			Class.forName(FlowStarter.class.getName());
			FlowManager.startFlow(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return 0;
	}

//	/**
//	 * 
//	 * @return
//	 */
//	public int startFlow(boolean sync) {
//		
//		if(!sync) {
//			return startFlow();
//		}
//		
//		try {
//			Class.forName(FlowStarter.class.getName());
//			FlowManager.startFlow(this);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		return 0;
//	}
}
