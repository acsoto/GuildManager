package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerCmd {

    public void sendConsoleCmd(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public void createResidence(String guildID, Player player) {
        player.setOp(true);
        player.chat("/resadmin create guild_" + guildID);
        player.setOp(false);
    }

}
