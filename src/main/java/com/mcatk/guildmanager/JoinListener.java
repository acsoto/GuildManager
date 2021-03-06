package com.mcatk.guildmanager;

import com.mcatk.guildmanager.msgs.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private GuildManager plugin;
    private Guilds guilds;
    
    JoinListener(GuildManager plugin) {
        this.plugin = plugin;
        this.guilds = plugin.getGuilds();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerID = player.getName();
        Guild guild = guilds.getPlayersGuild(playerID);
        if (guild != null) {
            if (guild.getChairman().equals(playerID)) {
                plugin.getServer().broadcastMessage(
                        Message.INFO + guild.getName() + "§2会长§e" + player.getName() + "§2已上线"
                );
            }
            if (plugin.getServer().getOnlinePlayers() != null) {
                StringBuilder msg = new StringBuilder(
                        Message.INFO + "§2您的公会" + guild.getName() + "§2现在在线玩家:"
                );
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (guild.hasPlayer(p.getName())) {
                        msg.append("§e").append(p.getName()).append(",");
                    }
                }
                player.sendMessage(msg.toString());
            }
            if (guild.isAdvancedPlayer(player.getName())) {
                player.sendMessage(Message.INFO + "§2您是公会高级成员，可以前往公会广场");
            }
            if (!guild.isMsgBoardEmpty()) {
                player.sendMessage("§2公会留言板为空");
            }
        }
    }
}
