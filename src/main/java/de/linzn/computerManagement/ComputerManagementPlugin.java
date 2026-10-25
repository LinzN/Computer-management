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

package de.linzn.computerManagement;

import de.linzn.computerManagement.webapi.ComputerWebapiHandler;
import de.linzn.simplyConfiguration.FileConfiguration;
import de.linzn.simplyConfiguration.provider.YamlConfiguration;
import de.linzn.stem.modules.pluginModule.STEMPlugin;

import java.io.File;

public class ComputerManagementPlugin extends STEMPlugin {

    public static ComputerManagementPlugin computerManagementPlugin;
    private ComputerManager computerManager;
    private ComputerWebapiHandler computerWebapiHandler;

    public ComputerManagementPlugin() {
        computerManagementPlugin = this;
    }

    @Override
    public void onEnable() {
        setUpConfig();
        this.computerManager = new ComputerManager(this);
        this.computerWebapiHandler = new ComputerWebapiHandler(this);
        this.getStemApi().commands().register("computer", new ComputerCommand());
    }

    @Override
    public void onDisable() {
        if (this.computerWebapiHandler != null) {
            this.computerWebapiHandler.disable();
        }
    }

    private void setUpConfig() {
        this.getDefaultConfig().save();
        File folder = new File(this.getDataFolder(), "devices");
        if (!folder.exists() && folder.mkdirs()) {
            File exampleFile = new File(folder, "example.yml");
            FileConfiguration exampleConfigFile = YamlConfiguration.loadConfiguration(exampleFile);
            exampleConfigFile.set("device.name", "test");
            exampleConfigFile.set("device.osType", OSType.WINDOWS.name());
            exampleConfigFile.set("device.macAddress", "FF:FF:FF:FF:FF:FF");
            exampleConfigFile.set("device.staticIP", "10.0.0.1");
            exampleConfigFile.set("device.staticSSHPort", 22);
            exampleConfigFile.set("device.staticSSHUser", "testUser");
            exampleConfigFile.save();
        }
    }

    public ComputerManager getComputerManager() {
        return computerManager;
    }

    public ComputerWebapiHandler getComputerWebapiHandler() {
        return computerWebapiHandler;
    }
}
