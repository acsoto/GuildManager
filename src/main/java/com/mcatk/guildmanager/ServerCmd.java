package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerCmd {
    
    public static void sendConsoleCmd(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
    
    public static void addPermission(String player, String permission) {
        sendConsoleCmd("lp user " + player + " permission set " + permission + " true");
    }
    
    public static void removePermission(String player, String permission) {
        sendConsoleCmd("lp user " + player + " permission clear " + permission);
    }
    
    public static void createResidence(Guild guild, Player player) {
        player.setOp(true);
        player.chat("/resadmin select vert");
        player.chat("/resadmin create guild_" + guild.getId());
        player.setOp(false);
    }
    
}
