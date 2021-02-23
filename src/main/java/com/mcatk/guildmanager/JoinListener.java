package com.mcatk.guildmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinListener implements Listener {
    GuildManager plugin;
    Guilds guilds;
    String MsgPrefix = "§6§l公会系统 §7>>> §a";
    
    JoinListener(GuildManager plugin, Guilds guilds) {
        this.plugin = plugin;
        this.guilds = guilds;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerID = player.getName();
        Guild guild = guilds.getChairmansGuild(playerID);
        if (guild != null) {
            plugin.getServer().broadcastMessage(MsgPrefix + guild.getName() + "§2会长§e" + player.getName() + "§2已上线");
        }
        //check if player has guild,if not return
        guild = guilds.getPlayersGuild(player.getName());
        if (guild == null)
            return;
        
        //tell player who is online in the guild
        if (plugin.getServer().getOnlinePlayers() != null) {
            StringBuilder msg = new StringBuilder(MsgPrefix + "§2您的公会" + guild.getName() + "§2现在在线玩家:");
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (guild.hasPlayer(p.getName())) {
                    msg.append("§e").append(p.getName()).append(",");
                }
            }
            player.sendMessage(msg.toString());
        }
        //tell player he is advanced
        if (guild.isAdvancedPlayer(player.getName())) {
            player.sendMessage(MsgPrefix + "§2您是公会高级成员，可以前往公会广场");
        }
        //tell player situation about msgBoard if it is not empty
        if (guild.isMsgBoardEmpty()) {
            player.sendMessage("§2公会留言板为空");
        } else player.sendMessage(guild.getMsgFromBoard());
    }
}
