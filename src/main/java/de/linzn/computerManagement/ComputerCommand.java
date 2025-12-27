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

import de.linzn.computerManagement.actions.ShutdownDevice;
import de.linzn.computerManagement.actions.WakeupDevice;
import de.linzn.stem.STEMApp;
import de.linzn.stem.modules.commandModule.ICommand;

public class ComputerCommand implements ICommand {
    @Override
    public boolean executeTerminal(String[] args) {
        if (args.length > 1) {
            Computer computer = ComputerManagementPlugin.computerManagementPlugin.getComputerManager().getComputerByName(args[1]);
            if (computer == null) {
                STEMApp.LOGGER.WARNING("No computer found with name " + args[1] + " in device folder!");
                return true;
            }

            if (args[0].equalsIgnoreCase("status")) {
                STEMApp.LOGGER.INFO("Status: " + computer.getDeviceStatus().name());
            } else if (args[0].equalsIgnoreCase("wakeup")) {
                new WakeupDevice(computer).runAction();
                STEMApp.LOGGER.INFO("Wakeup signal send!");
            } else if (args[0].equalsIgnoreCase("shutdown")) {
                new ShutdownDevice(computer).runAction();
                STEMApp.LOGGER.INFO("Shutdown signal send!");
            } else {
                STEMApp.LOGGER.WARNING("No action found with name " + args[0] + "!");
            }
        }
        return true;
    }
}
