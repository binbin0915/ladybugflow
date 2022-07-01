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
package io.github.nobuglady.network.fw.util.model;

import java.util.ArrayList;
import java.util.List;

import io.github.nobuglady.network.fw.constant.NodeStatus;
import io.github.nobuglady.network.fw.constant.NodeStatusDetail;

/**
 * 
 * @author NoBugLady
 *
 */
public class Convert2JsonFlowDto {

	public List<Convert2JsonNodeDto> nodes = new ArrayList<Convert2JsonNodeDto>();
	public List<Convert2JsonEdgeDto> edges = new ArrayList<Convert2JsonEdgeDto>();

	public String toJson() {

		Convert2JsonEdgeDto startEdge = null;
		for (Convert2JsonEdgeDto edge : edges) {
			if (edge.from.equals("start")) {
				startEdge = edge;
				break;
			}
		}

		if (startEdge != null) {
			edges.remove(startEdge);
			edges.add(0, startEdge);
		}

		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("\"nodes\":[");

		for (Convert2JsonNodeDto jobNodeDto : nodes) {
			json.append("{");
			// id
			json.append("\"id\": \"" + jobNodeDto.id + "\",");
			// label
			json.append("\"label\": \"" + jobNodeDto.label + "\" ");

			// color
			// wait E8F9FD
			// ready 79DAE8
			// open 187498
			// running F9D923
			// success 36AE7C
			// error EB5353

			if (NodeStatus.SKIPED == jobNodeDto.status) {
				json.append(",");
				json.append("\"color\": \"#999999\"");
			} else if (NodeStatus.WAIT == jobNodeDto.status) {
				json.append(",");
				json.append("\"color\": \"#E8F9FD\"");
			} else if (NodeStatus.READY == jobNodeDto.status) {
				json.append(",");
				json.append("\"color\": \"#79DAE8\"");
			} else if (NodeStatus.OPENNING == jobNodeDto.status) {
				json.append(",");
				json.append("\"color\": \"#187498\"");
			} else if (NodeStatus.RUNNING == jobNodeDto.status) {
				json.append(",");
				json.append("\"color\": \"#F9D923\"");
			} else if (NodeStatus.COMPLETE == jobNodeDto.status || NodeStatus.GO == jobNodeDto.status) {

				if (NodeStatusDetail.COMPLETE_SUCCESS == jobNodeDto.status_detail) {
					json.append(",");
					json.append("\"color\": \"#36AE7C\"");
				} else if (NodeStatusDetail.COMPLETE_ERROR == jobNodeDto.status_detail) {
					json.append(",");
					json.append("\"color\": \"#EB5353\"");
				} else if (NodeStatusDetail.COMPLETE_TIMEOUT == jobNodeDto.status_detail) {
					json.append(",");
					json.append("\"color\": \"#EB5353\"");
				} else if (NodeStatusDetail.COMPLETE_CANCEL == jobNodeDto.status_detail) {
					json.append(",");
					json.append("\"color\": \"#EB5353\"");
				}

			}

			json.append("}");
			json.append(",");
		}

		if (nodes.size() > 0) {
			// delete last ","
			json.deleteCharAt(json.length() - 1);
		}

		json.append("],\"edges\":[");

		for (Convert2JsonEdgeDto jobEdgeDto : edges) {
			json.append("{");
			json.append("\"id\": \"" + jobEdgeDto.id + "\",");
			json.append("\"from\": \"" + jobEdgeDto.from + "\",");
			json.append("\"to\": \"" + jobEdgeDto.to + "\",");
			json.append("\"arrows\": \"to\"");
			json.append("}");
			json.append(",");
		}

		if (edges.size() > 0) {
			// delete last ","
			json.deleteCharAt(json.length() - 1);
		}

		json.append("]}");
		return json.toString();
	}
}
