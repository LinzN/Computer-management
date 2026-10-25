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
    private volatile DeviceStatus deviceStatus;

    public Computer(String name, OSType osType, String macAddress, String staticIP, int staticSSHPort, String staticSSHUsername) {
        this.name = name;
        this.osType = osType;
        this.macAddress = macAddress;
        this.staticIP = staticIP;
        this.staticSSHPort = staticSSHPort;
        this.staticSSHUsername = staticSSHUsername;
        this.deviceStatus = DeviceStatus.UNKNOWN;
        ComputerManagementPlugin.computerManagementPlugin.getStemApi().scheduler().runRepeating(this::request_device_status, 1, 2, TimeUnit.SECONDS);
    }

    private void request_device_status() {
        DeviceStatus newStatus;
        try {
            newStatus = InetAddress.getByName(staticIP).isReachable(1500) ? DeviceStatus.ONLINE : DeviceStatus.OFFLINE;
        } catch (IOException e) {
            newStatus = DeviceStatus.UNKNOWN;
        }
        if (this.deviceStatus != newStatus) {
            ComputerManagementPlugin.computerManagementPlugin.getPluginLogger().info("Computer " + this.name + " changed status to " + newStatus.name());
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
