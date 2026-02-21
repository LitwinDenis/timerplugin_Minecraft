package org.denis.timerplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class Timerplugin extends JavaPlugin implements CommandExecutor {

    private boolean running = false;
    private int timeElapsed = 0;
    private BukkitRunnable timerTask;

    @Override
    public void onEnable() {
        if (getCommand("starttimer") != null) getCommand("starttimer").setExecutor(this);
        if (getCommand("stoptimer") != null) getCommand("stoptimer").setExecutor(this);
        if (getCommand("resettimer") != null) getCommand("resettimer").setExecutor(this);

        saveDefaultConfig();

        timeElapsed = getConfig().getInt("timeElapsed", 0);
        running = getConfig().getBoolean("running", false);

        if (running) {
            startTimer();
        }
    }
    @Override
    public void onDisable() {
        getConfig().set("timeElapsed", timeElapsed);
        getConfig().set("running", running);
        saveConfig();
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
            if(timeElapsed == 0) {
                sender.sendMessage(Component.text("Timer started!", NamedTextColor.GREEN));
            }else{
                sender.sendMessage(Component.text("Timer continues running !", NamedTextColor.GREEN));
            }
            return true;
        }
        if (label.equalsIgnoreCase("stoptimer")) {
            if (!running) {
                sender.sendMessage(Component.text("Timer not running please Start!", NamedTextColor.RED));
                return true;
            }

            running = false;
            sender.sendMessage(Component.text("Timer stopped " + formatTime(timeElapsed), NamedTextColor.YELLOW));
            return true;
        }
        if (label.equalsIgnoreCase("resettimer")) {
            running = false;
            timeElapsed = 0;
            sender.sendMessage(Component.text("Timer set to 0", NamedTextColor.GREEN));

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(Component.text(""));
            }
            return true;
        }
        return false;
    }

    private void startTimer() {
        new BukkitRunnable() {

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
        }.runTaskTimer(this, 0L, 20L);
    }

    private String formatTime(int totalseconds) {

        int days = totalseconds / 86400;
        int hours = (totalseconds % 86400) / 3600;
        int minutes = (totalseconds % 3600) / 60;
        int seconds = totalseconds % 60;

        if (days > 0) {
            return String.format("%ddd %02dh %02dm %02ds", days, hours, minutes, seconds);
        } else {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        }

    }
}

