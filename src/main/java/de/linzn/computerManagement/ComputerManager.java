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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComputerManager {
    private final STEMPlugin stemPlugin;
    private final Map<String, Computer> computerMap;

    public ComputerManager(STEMPlugin stemPlugin) {
        this.stemPlugin = stemPlugin;
        this.computerMap = new ConcurrentHashMap<>();
        this.loadComputers();
    }

    private void loadComputers() {
        File folder = new File(stemPlugin.getDataFolder(), "devices");
        File[] files = folder.listFiles((dir, fileName) -> fileName.endsWith(".yml") && !fileName.equalsIgnoreCase("example.yml"));
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                FileConfiguration computerConfig = YamlConfiguration.loadConfiguration(file);
                String name = computerConfig.getString("device.name");
                if (name == null || name.isBlank()) {
                    stemPlugin.getPluginLogger().warning("Device config " + file.getName() + " has no name, skipping");
                    continue;
                }
                OSType osType = OSType.valueOf(String.valueOf(computerConfig.getString("device.osType")).toUpperCase(Locale.ROOT));
                Computer computer = new Computer(name, osType, computerConfig.getString("device.macAddress"),
                        computerConfig.getString("device.staticIP"), computerConfig.getInt("device.staticSSHPort"),
                        computerConfig.getString("device.staticSSHUser"));
                this.computerMap.put(computer.getName().toLowerCase(Locale.ROOT), computer);
            } catch (IllegalArgumentException e) {
                stemPlugin.getPluginLogger().warning("Invalid device config " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    public Computer getComputerByName(String name) {
        return name == null ? null : this.computerMap.get(name.toLowerCase(Locale.ROOT));
    }
}
