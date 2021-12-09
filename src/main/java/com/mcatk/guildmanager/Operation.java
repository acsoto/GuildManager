package com.mcatk.guildmanager;

import com.mcatk.guildmanager.models.Guild;
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
        sendCmdWithOp(player, "setwarp " + guild.getGuildName());
    }
    
    //公会传送指令
    public void tpGuild(Guild guild, String player) {
        sendConsoleCmd("warp " + guild.getGuildName() + " " + player);
    }
    
    public void delWarp(Guild guild) {
        sendConsoleCmd("delwarp " + guild);
    }

    public void giveGuildSquarePerm(String player) {
        new ServerCmd().sendConsoleCmd("res pset main.gh " + player + " move true");
    }

    public void removeGuildSquarePerm(String player) {
        new ServerCmd().sendConsoleCmd("res pset main.gh " + player + " move remove");
    }
}
