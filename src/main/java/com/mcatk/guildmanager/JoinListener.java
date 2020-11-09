package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    GuildManager plugin = GuildManager.plugin;
    //String MsgPrefix = "§d§l系统 §7>>> §a";
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Guild guild = plugin.getChairmansGuild(player.getName());
        if(guild!=null){
            Bukkit.broadcastMessage("§2"+guild.getName()+"会长"+player.getName()+"已上线");
            return;
        }
        guild = plugin.getPlayersGuild(player.getName());
        if(guild!=null){
            player.sendMessage("§2"+guild.getName()+"§2现在在线玩家:");
            for (String p : guild.getMembers()){
                player.sendMessage("§a"+p);
            }
        }
    }
}
