# 🕒 TimerPlugin

A lightweight and persistent **Minecraft Timer Plugin** designed for Paper/Spigot servers. This plugin displays a stopwatch directly in the **Action Bar** (above the hotbar) and ensures that the time is saved even across server restarts or crashes.

## ✨ Features

* **Action Bar Display**: The timer is shown non-intrusively in **Aqua & Bold** styling above the player's hotbar.
* **Smart Time Formatting**:
* Below 24 hours: `00h 05m 30s`
* Above 24 hours: `1d 02h 15m 45s`


* **Persistent Storage**: Automatically saves the current time and running state to `config.yml`. If the server reboots, the timer resumes exactly where it left off.
* **Simple Controls**: Three intuitive commands to manage your stopwatch.

---

## 🛠 Commands & Usage

| Command | Description |
| --- | --- |
| **`/starttimer`** | Starts the stopwatch or resumes it from a pause. |
| **`/stoptimer`** | Pauses the current timer. |
| **`/resettimer`** | Stops the timer and resets the value to `0`. |

> [!IMPORTANT]
> By default, these commands are available to all players. You can restrict them using a permission plugin (like LuckPerms) or by adding requirements to the code.

---

## 📥 Installation

1. Ensure your server is running **Paper** or **Spigot** (Version 1.21+).
2. Build the project using Maven to get the `timerplugin-1.0-SNAPSHOT.jar`.
3. Upload the `.jar` file to your `/plugins` folder (e.g., via the **Crafty Dashboard**).
4. Restart the server or use `/reload confirm`.

---

## ⚙️ Configuration

A `config.yml` file is automatically created in `/plugins/timerplugin/` after the first run. This file handles the persistence:

```yaml
# The current time value in seconds
timeElapsed: 0
# Whether the timer should resume automatically on startup
running: false

```

---

## 💻 Technical Details

* **API**: Paper-API 1.21.1
* **Language**: Java 21
* **Build Tool**: Maven
* **Style**: Adventure API (Component-based)

---

