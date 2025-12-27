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

import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.webapi.WebApiPlugin;
import de.linzn.webapi.modules.WebModule;

public class ComputerWebapiHandler {

    private STEMPlugin stemPlugin;

    public ComputerWebapiHandler(STEMPlugin stemPlugin) {
        this.stemPlugin = stemPlugin;
        this.registerHandler();
    }

    private void registerHandler() {
        WebModule computerWebApiModule = new WebModule("computermanagement");
        computerWebApiModule.registerSubCallHandler(new WakeupComputer(), "wakeup");
        computerWebApiModule.registerSubCallHandler(new ShutdownComputer(), "shutdown");
        computerWebApiModule.registerSubCallHandler(new StatusComputer(), "status");
        WebApiPlugin.webApiPlugin.getWebServer().enableCallModule(computerWebApiModule);
    }
}
