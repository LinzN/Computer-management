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

import de.linzn.simplyConfiguration.FileConfiguration;
import de.linzn.simplyConfiguration.provider.YamlConfiguration;
import de.linzn.stem.modules.pluginModule.STEMPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ComputerManager {
    private final STEMPlugin stemPlugin;
    private final Map<String, Computer> computerMap;

    public ComputerManager(STEMPlugin stemPlugin) {
        this.stemPlugin = stemPlugin;
        this.computerMap = new HashMap<>();
        ;
        this.loadComputers();
    }

    private void loadComputers() {
        File folder = new File(ComputerManagementPlugin.computerManagementPlugin.getDataFolder(), "devices");

        if (folder.exists()) {
            for (final File file : folder.listFiles()) {
                if (file.getName().equalsIgnoreCase("example.yml")) {
                    continue;
                }

                FileConfiguration computerConfig = YamlConfiguration.loadConfiguration(file);

                String name = computerConfig.getString("device.name");
                OSType osType = OSType.valueOf(computerConfig.getString("device.osType"));
                String macAddress = computerConfig.getString("device.macAddress");
                String staticIP = computerConfig.getString("device.staticIP");
                int staticSSHPort = computerConfig.getInt("device.staticSSHPort");
                String statusSSHUser = computerConfig.getString("device.staticSSHUser");

                Computer computer = new Computer(name, osType, macAddress, staticIP, staticSSHPort, statusSSHUser);
                this.computerMap.put(computer.getName().toLowerCase(), computer);
            }
        }
    }

    public Computer getComputerByName(String name) {
        return this.computerMap.get(name.toLowerCase());
    }
}
