![MirraNET Logo](https://raw.githubusercontent.com/LinzN/Stem/master/stem-core/src/main/resources/header-logo.png)

# computer-management

> Wake up, shut down and monitor computers from [STEM](https://github.com/LinzN/Stem).

computer-management knows the computers in the network, checks every two seconds whether they are online
and can wake them with Wake-on-LAN or shut them down via SSH, from the STEM console or through
[web-api](https://github.com/LinzN/web-api).

---

## Features

- Online status (`ONLINE`, `OFFLINE`, `UNKNOWN`) of every computer, checked every 2 seconds
- Wake-on-LAN with `etherwake` (the MAC address is validated before it is passed to the shell)
- Shutdown via SSH for Windows (`shutdown -s -f -t 5`) and Linux (`shutdown -h now`)
- Console command and web endpoints

---

## Requirements

| Requirement | Version                                                  |
|-------------|----------------------------------------------------------|
| STEM        | 1.2.0 or newer (`stem-api`)                              |
| Plugins     | [web-api](https://github.com/LinzN/web-api)              |
| Host        | `etherwake` on the STEM host; SSH key login from STEM to every computer |

---

## Installation

1. Build the plugin with `mvn clean package` (or take the build from Jenkins).
2. Copy `target/computer-management.jar` into the `plugins` folder of STEM.
3. Start STEM once. The plugin creates `plugins/computer-management/devices/example.yml`.
4. Add one file per computer and restart STEM (plugins are only loaded at startup).

---

## Configuration

One file per computer in `plugins/computer-management/devices/` (`example.yml` is ignored):

```yaml
device:
  name: workstation          # name used in commands and web requests (case insensitive)
  osType: WINDOWS            # WINDOWS or LINUX
  macAddress: "AA:BB:CC:DD:EE:FF"
  staticIP: 10.0.0.50
  staticSSHPort: 22
  staticSSHUser: admin
```

---

## Commands

| Command                       | Description                      |
|-------------------------------|----------------------------------|
| `computer status <name>`      | Shows the online status          |
| `computer wakeup <name>`      | Sends a Wake-on-LAN packet       |
| `computer shutdown <name>`    | Shuts the computer down via SSH  |

---

## Web API

All requests are `POST` with a JSON body.

| Path                             | Body                                                   | Result                       |
|----------------------------------|--------------------------------------------------------|------------------------------|
| `/computermanagement/status`     | `{"computerName": "workstation", "requestAction": "READ"}`  | `{"state": "ONLINE", ...}` |
| `/computermanagement/wakeup`     | `{"computerName": "workstation", "requestAction": "WRITE"}` | sends Wake-on-LAN           |
| `/computermanagement/shutdown`   | `{"computerName": "workstation", "requestAction": "WRITE"}` | shuts the computer down     |

An unknown computer name returns `apiError: true`.

---

## License

Copyright (c) MirraNET, Niklas Linz. Licensed under the
[GNU Lesser General Public License v3.0](https://www.gnu.org/licenses/lgpl-3.0.html).
