package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Operation {
    //console command
    public void sendConsoleCmd(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
    
    public void sendCmdWithOp(Player player, String cmd) {
        if (player.isOp()) {
            player.chat("/" + cmd);
        } else {
            player.setOp(true);
            player.chat("/" + cmd);
            player.setOp(false);
        }
    }
    
    public void setWarp(Player player, Guild guild) {
        sendCmdWithOp(player, "setwarp " + guild.getName());
    }
    
    //公会传送指令
    public void tpGuild(Guild guild, String player) {
        sendConsoleCmd("warp " + guild.getName() + " " + player);
    }
    
    public void delWarp(Guild guild) {
        sendConsoleCmd("delwarp " + guild);
    }
}
