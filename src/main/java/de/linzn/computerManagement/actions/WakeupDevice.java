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

package de.linzn.computerManagement.actions;

import de.linzn.computerManagement.Computer;
import de.linzn.computerManagement.ComputerManagementPlugin;
import de.linzn.stem.taskManagment.operations.defaultOperations.ShellOperation;

import java.util.Locale;
import java.util.regex.Pattern;

public class WakeupDevice implements DeviceActions {
    private static final Pattern MAC_PATTERN = Pattern.compile("^([0-9a-fA-F]{2}[:-]){5}[0-9a-fA-F]{2}$");
    private final Computer computer;

    public WakeupDevice(Computer computer) {
        this.computer = computer;
    }

    @Override
    public void runAction() {
        String mac = computer.getMacAddress();
        /* the MAC is passed to a shell command, so only accept a well-formed address */
        if (mac == null || !MAC_PATTERN.matcher(mac).matches()) {
            ComputerManagementPlugin.computerManagementPlugin.getPluginLogger().error("Invalid MAC address for " + computer.getName() + ": " + mac);
            return;
        }
        ShellOperation shellOperation = new ShellOperation();
        shellOperation.setUseOutput(false);
        shellOperation.setUseSSH(false);
        shellOperation.setScriptCommand("etherwake " + mac.toLowerCase(Locale.ROOT).replace('-', ':'));
        ComputerManagementPlugin.computerManagementPlugin.getStemApi().scheduler().runAsync(shellOperation);
    }
}
