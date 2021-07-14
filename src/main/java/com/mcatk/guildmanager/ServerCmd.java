package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerCmd {
    
    public void sendConsoleCmd(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
    
    public void addPermission(String player, String permission) {
        sendConsoleCmd("lp user " + player + " permission set " + permission + " true");
    }
    
    public void removePermission(String player, String permission) {
        sendConsoleCmd("lp user " + player + " permission clear " + permission);
    }
    
    public void createResidence(Guild guild, Player player) {
        player.setOp(true);
        player.chat("/resadmin create guild_" + guild.getId());
        player.setOp(false);
    }
    
}
