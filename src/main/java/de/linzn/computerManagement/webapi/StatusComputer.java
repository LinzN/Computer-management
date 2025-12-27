/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.computerManagement.webapi;

import com.sun.net.httpserver.HttpExchange;
import de.linzn.computerManagement.Computer;
import de.linzn.computerManagement.ComputerManagementPlugin;
import de.linzn.webapi.core.ApiResponse;
import de.linzn.webapi.core.HttpRequestClientPayload;
import de.linzn.webapi.modules.RequestInterface;
import org.json.JSONObject;

import java.io.IOException;

public class StatusComputer extends RequestInterface {
    @Override
    public Object callHttpEvent(HttpExchange httpExchange, HttpRequestClientPayload httpRequestClientPayload) throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        JSONObject postData = (JSONObject) httpRequestClientPayload.getPostData();

        String computerName = postData.get("computerName").toString();
        String requestAction = postData.get("requestAction").toString();

        Computer computer = ComputerManagementPlugin.computerManagementPlugin.getComputerManager().getComputerByName(computerName);

        if (computer != null) {
            if (requestAction.equalsIgnoreCase("READ")) {
                apiResponse.getJSONObject().put("state", computer.getDeviceStatus().name());
            }
        } else {
            apiResponse.setError("Computer with this name not found!");
        }
        return apiResponse.buildResponse();
    }
}
