package com.mcatk.guildmanager.sql;

import com.mcatk.guildmanager.GuildManager;
import org.bukkit.Bukkit;

public class SQLUpdater {
    public void run() {
        Bukkit.getScheduler().runTaskTimer(GuildManager.getPlugin(), () -> {
            SQLManager.getInstance().update();
            GuildManager.getPlugin().getLogger().info("公会数据已更新");
        }, 20L * 60L, 20L * 60L);
    }
}
