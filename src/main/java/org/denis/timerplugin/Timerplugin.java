package org.denis.timerplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.denis.plugin.Dashboard;

public final class Timerplugin extends JavaPlugin implements CommandExecutor {

    private boolean running = false;
    private int timeElapsed = 0;
    private BukkitRunnable timerTask;

    @Override
    public void onEnable() {
        if (getCommand("starttimer") != null) getCommand("starttimer").setExecutor(this);
        if (getCommand("stoptimer") != null) getCommand("stoptimer").setExecutor(this);
        if (getCommand("resettimer") != null) getCommand("resettimer").setExecutor(this);


        Bukkit.getServicesManager().register(Dashboard.Challenge.class, new TimerChallenge(this), this, ServicePriority.Normal);

        saveDefaultConfig();
        timeElapsed = getConfig().getInt("timeElapsed", 0);
        running = getConfig().getBoolean("running", false);

        if (running) {
            startTimer();
        }
    }
    private void registerDashboard() {
        Bukkit.getServicesManager().register(Dashboard.Challenge.class, new TimerChallenge(this), this, ServicePriority.Normal);
    }
    @Override
    public void onDisable() {
        getConfig().set("timeElapsed", timeElapsed);
        getConfig().set("running", running);
        saveConfig();
        if (timerTask != null) timerTask.cancel();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("starttimer")) {
            if (running) {
                sender.sendMessage(Component.text("Timer already running!", NamedTextColor.RED));
                return true;
            }
            running = true;
            startTimer();
            sender.sendMessage(Component.text(timeElapsed == 0 ? "Timer started!" : "Timer continues!", NamedTextColor.GREEN));
            return true;
        }

        if (label.equalsIgnoreCase("stoptimer")) {
            if (!running) {
                sender.sendMessage(Component.text("Timer not running!", NamedTextColor.RED));
                return true;
            }
            running = false;
            sender.sendMessage(Component.text("Timer stopped at " + formatTime(timeElapsed), NamedTextColor.YELLOW));
            return true;
        }

        if (label.equalsIgnoreCase("resettimer")) {
            running = false;
            timeElapsed = 0;
            sender.sendMessage(Component.text("Timer set to 0", NamedTextColor.GREEN));
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(Component.empty());
            }
            return true;
        }
        return false;
    }

    public void startTimer() {
        if (timerTask != null) timerTask.cancel();
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!running) {
                    this.cancel();
                    return;
                }
                Component message = Component.text(formatTime(timeElapsed), NamedTextColor.DARK_PURPLE, TextDecoration.BOLD);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendActionBar(message);
                }
                timeElapsed++;
            }
        };
        timerTask.runTaskTimer(this, 0L, 20L);
    }

    private String formatTime(int totalSeconds) {
        int days = totalSeconds / 86400;
        int hours = (totalSeconds % 86400) / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (days > 0) {
            return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
        } else {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        }
    }
    public class TimerChallenge implements Dashboard.Challenge {
        private final Timerplugin plugin;
        public TimerChallenge(Timerplugin plugin) { this.plugin = plugin; }

        @Override public String getName() { return "Timer Challenge"; }
        @Override public Material getIcon() { return Material.CLOCK; }
        @Override public void start() { plugin.running = true; plugin.startTimer(); }
        @Override public void stop() { plugin.running = false; }
        @Override public boolean isRunning() { return plugin.running; }
    }
}