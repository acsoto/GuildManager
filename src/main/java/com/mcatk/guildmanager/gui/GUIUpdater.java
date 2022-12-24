package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.GuildManager;
import org.bukkit.Bukkit;

public class GUIUpdater {
    public void run() {
        Bukkit.getScheduler().runTaskTimer(GuildManager.getPlugin(), () -> {
            GuildManager.getPlugin().getLogger().info("公会GUI已更新");
            GuildManager.getPlugin().getGuildsGUI().updateGUI();
        }, 20L * 300L, 20L * 300L);
    }
}
