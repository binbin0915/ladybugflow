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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.nobuglady.network.fw.component.FlowComponentFactory;
import io.github.nobuglady.network.fw.component.IFlowAccessor;
import io.github.nobuglady.network.fw.constant.NodeStartType;
import io.github.nobuglady.network.fw.constant.NodeStatus;
import io.github.nobuglady.network.fw.logger.ConsoleLogger;
import io.github.nobuglady.network.fw.model.EdgeDto;
import io.github.nobuglady.network.fw.model.FlowDto;
import io.github.nobuglady.network.fw.model.NodeDto;
import io.github.nobuglady.network.fw.persistance.entity.FlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;
import io.github.nobuglady.network.fw.starter.FlowStarter;
import io.github.nobuglady.network.fw.util.StringUtil;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowRunnerHelper {

	private static ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();;

	private static Map<String, String> scheduledFlowIdCronMap = new HashMap<String, String>();

	private static Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<String, ScheduledFuture<?>>();

	private static IFlowAccessor flowAccessor = FlowComponentFactory.getFlowAccessor();

	static {
		taskScheduler.initialize();
	}

	/**
	 * startFlow
	 * 
	 * @param flowRunner   flowRunner
	 * @param jsonFileName jsonFileName
	 * @param startParam   startParam
	 * @return historyId
	 */
	public static String startFlow(FlowRunner flowRunner, String jsonFileName, String startParam) {

		String flowPath = flowRunner.getClass().getName();

		if (StringUtil.isNotEmpty(jsonFileName)) {
			flowPath = jsonFileName.replace(".json", "");
		}

		FlowEntity flow = createHistory(flowPath, startParam);
		String flowId = flow.flowEntity.getFlowId();
		String historyId = flow.flowEntity.getHistoryId();

		FlowStarter.flowRunnerMap.put(flowId + "," + historyId, flowRunner);

		List<HistoryNodeEntity> firstNodeList = getFirstNodeId(flow);

		if (firstNodeList != null) {
			for (HistoryNodeEntity firstNode : firstNodeList) {

				if (firstNode.getStartType() == NodeStartType.NODE_START_TYPE_TIMER) {
					String existCron = scheduledFlowIdCronMap.get(firstNode.getFlowId());

					if (existCron == null) {
						ScheduledFuture<?> future = register(firstNode.getStartCron(), flowRunner, jsonFileName,
								firstNode.getFlowId(), firstNode.getHistoryId(), firstNode.getNodeId(), startParam);
						scheduledFlowIdCronMap.put(firstNode.getFlowId(), firstNode.getStartCron());
						scheduledFutureMap.put(firstNode.getFlowId(), future);
					} else {
						if (existCron.equals(firstNode.getStartCron())) {
							System.out.println("already scheduled:" + firstNode.getFlowId());
						} else {
							boolean cancelResult = scheduledFutureMap.get(firstNode.getFlowId()).cancel(false);

							if (cancelResult) {
								ScheduledFuture<?> future = register(firstNode.getStartCron(), flowRunner, jsonFileName,
										firstNode.getFlowId(), firstNode.getHistoryId(), firstNode.getNodeId(),
										startParam);
								scheduledFlowIdCronMap.put(firstNode.getFlowId(), firstNode.getStartCron());
								scheduledFutureMap.put(firstNode.getFlowId(), future);
							} else {
								System.out.println("task cancel faild:" + firstNode.getFlowId());
							}

						}

					}

				} else {

					String firstNodeId = firstNode.getNodeId();
					flowAccessor.updateNodeStatusByNodeId(flowId, historyId, firstNodeId, NodeStatus.READY);
					startNode(flowId, historyId, firstNodeId);

				}
			}
		}

		return historyId;

	}

	/**
	 * register
	 * 
	 * @param cron         cron
	 * @param flowRunner   flowRunner
	 * @param jsonFileName jsonFileName
	 * @param flowId       flowId
	 * @param historyId    historyId
	 * @param nodeId       nodeId
	 * @param startParam   startParam
	 * @return ScheduledFuture
	 */
	private static ScheduledFuture<?> register(String cron, FlowRunner flowRunner, String jsonFileName, String flowId,
			String historyId, String nodeId, String startParam) {

		if (!CronExpression.isValidExpression(cron)) {
			System.out.println("not a valied expression:" + cron);
			return null;
		}

		CronExpression exp = CronExpression.parse(cron);
		LocalDateTime nextTime = exp.next(LocalDateTime.now());

		if (nextTime != null) {
			System.out.println("[" + flowId + "] next execute time:"
					+ nextTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		}

		return taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {

//				FlowRunner flowRunnerNew = flowRunner.getClass().newInstance();
				FlowRunner flowRunnerNew = flowRunner;

				String flowPath = flowRunnerNew.getClass().getName();

				if (StringUtil.isNotEmpty(jsonFileName)) {
					flowPath = jsonFileName.replace(".json", "");
				}

				FlowEntity flow = createHistory(flowPath, startParam);
				String flowId = flow.flowEntity.getFlowId();
				String historyId = flow.flowEntity.getHistoryId();

				FlowStarter.flowRunnerMap.put(flowId + "," + historyId, flowRunnerNew);

				flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeId, NodeStatus.READY);
				startNode(flowId, historyId, nodeId);

			}
		}, new CronTrigger(cron));
	}

	/**
	 * createHistory
	 * 
	 * @param flowPath   flowPath
	 * @param startParam startParam
	 * @return FlowEntity
	 */
	private static FlowEntity createHistory(String flowPath, String startParam) {

		String historyId = flowAccessor.createHistoryId();

		FlowEntity flow = loadJson(flowPath, historyId, startParam);
		flowAccessor.saveFlow(flow);

		return flow;
	}

	/**
	 * loadJson
	 * 
	 * @param flowPath   flowPath
	 * @param historyId  historyId
	 * @param startParam startParam
	 * @return FlowEntity
	 */
	private static FlowEntity loadJson(String flowPath, String historyId, String startParam) {

		FlowEntity flowEntityDB = new FlowEntity();

		try (Reader reader = new InputStreamReader(
				FlowRunnerHelper.class.getResourceAsStream("/" + flowPath.replace(".", "/") + ".json"))) {

			ObjectMapper mapper = new ObjectMapper();
			FlowDto flowDto = mapper.readValue(reader, FlowDto.class);

			HistoryFlowEntity flowEntity = new HistoryFlowEntity();
			flowEntity.setFlowId(flowDto.flowId);
			flowEntity.setHistoryId(historyId);
			flowEntity.setStartParam(startParam);

			ConsoleLogger logger = ConsoleLogger.getInstance(flowDto.flowId, historyId);

			flowEntityDB.flowEntity = flowEntity;

			if (flowDto.nodes != null) {
				for (NodeDto nodeDto : flowDto.nodes) {
					HistoryNodeEntity nodeEntity = new HistoryNodeEntity();
					nodeEntity.setHistoryId(historyId);
					nodeEntity.setFlowId(flowEntity.getFlowId());
					nodeEntity.setNodeId(nodeDto.id);
					nodeEntity.setNodeName(nodeDto.label);
					nodeEntity.setReadyCheck(nodeDto.readyCheck);
					nodeEntity.setStartType(nodeDto.startType);
					nodeEntity.setExecuteType(nodeDto.executeType);
					nodeEntity.setStartCron(nodeDto.startCron);

					flowEntityDB.nodeEntityList.add(nodeEntity);
				}
			}

			if (flowDto.edges != null) {
				for (EdgeDto edgeDto : flowDto.edges) {
					HistoryEdgeEntity edgeEntity = new HistoryEdgeEntity();
					edgeEntity.setFlowId(flowEntity.getFlowId());
					edgeEntity.setHistoryId(historyId);
					edgeEntity.setEdgeId(edgeDto.id);
					edgeEntity.setFromNodeId(edgeDto.from);
					edgeEntity.setToNodeId(edgeDto.to);
					edgeEntity.setEdgeCondition(edgeDto.condition);
					flowEntityDB.edgeEntityList.add(edgeEntity);
				}

			}

			String json = mapper.writeValueAsString(flowDto);
			logger.info("json:\n" + json);

			return flowEntityDB;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	//////////////////////////////
	// help function
	//////////////////////////////

	/**
	 * getFirstNodeId
	 * 
	 * @param flow flow
	 * @return HistoryNodeEntity
	 */
	private static List<HistoryNodeEntity> getFirstNodeId(FlowEntity flow) {

		List<HistoryNodeEntity> resultList = new ArrayList<>();

		resultList.addAll(flow.nodeEntityList);

		Set<String> toNodeIdSet = new HashSet<String>();
		for (HistoryEdgeEntity edgeEntity : flow.edgeEntityList) {
			toNodeIdSet.add(edgeEntity.getToNodeId());
		}

		for (int i = resultList.size() - 1; i >= 0; i--) {

			HistoryNodeEntity nodeEntity = resultList.get(i);

			if (toNodeIdSet.contains(nodeEntity.getNodeId())) {
				resultList.remove(i);
			}
		}
		return resultList;
	}

	/**
	 * startNode
	 * 
	 * @param flowId    flowId
	 * @param historyId historyId
	 * @param nodeId    nodeId
	 */
	private static void startNode(String flowId, String historyId, String nodeId) {

		flowAccessor.updateNodeStatusByNodeId(flowId, historyId, nodeId, NodeStatus.RUNNING);
		FlowComponentFactory.getReadyQueueSender().putReadyNode(flowId, historyId, nodeId);
	}

}
