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

import de.linzn.stem.STEMApp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class Computer {

    private final String name;
    private final OSType osType;
    private final String macAddress;
    private final String staticIP;
    private final String staticSSHUsername;
    private final int staticSSHPort;
    private DeviceStatus deviceStatus;

    public Computer(String name, OSType osType, String macAddress, String staticIP, int staticSSHPort, String staticSSHUsername) {
        this.name = name;
        this.osType = osType;
        this.macAddress = macAddress;
        this.staticIP = staticIP;
        this.staticSSHPort = staticSSHPort;
        this.staticSSHUsername = staticSSHUsername;
        this.deviceStatus = DeviceStatus.UNKNOWN;
        STEMApp.getInstance().getScheduler().runRepeatScheduler(ComputerManagementPlugin.computerManagementPlugin, this::request_device_status, 1, 2, TimeUnit.SECONDS);
    }

    private void request_device_status() {
        DeviceStatus newStatus;
        try {
            if (InetAddress.getByName(staticIP).isReachable(1500)) {
                newStatus = DeviceStatus.ONLINE;
            } else {
                newStatus = DeviceStatus.OFFLINE;
            }
        } catch (IOException e) {
            newStatus = DeviceStatus.UNKNOWN;
            STEMApp.LOGGER.ERROR(e);
        }
        if (this.deviceStatus != newStatus) {
            STEMApp.LOGGER.INFO("Computer " + this.name + " changed status to " + newStatus.name());
        }
        this.deviceStatus = newStatus;
    }


    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getStaticIP() {
        return staticIP;
    }

    public int getStaticSSHPort() {
        return staticSSHPort;
    }

    public OSType getOsType() {
        return osType;
    }

    public String getStaticSSHUsername() {
        return staticSSHUsername;
    }

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }
}
