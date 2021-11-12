package com.mcatk.guildmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private Guilds guilds;

    JoinListener() {
        this.guilds = GuildManager.getPlugin().getGuilds();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerID = player.getName();
        Guild guild = guilds.getPlayersGuild(playerID);
        if (guild != null) {
            // 玩家有公会
            if (guild.getChairman().equals(playerID)) {
                GuildManager.getPlugin().getServer().broadcastMessage(
                        Msg.INFO + guild.getName() + "§2会长§e" + player.getName() + "§2已上线"
                );
                new Operation().giveGuildSquarePerm(playerID);
            }
            if (GuildManager.getPlugin().getServer().getOnlinePlayers() != null) {
                StringBuilder msg = new StringBuilder(
                        Msg.INFO + "§2您的公会" + guild.getName() + "§2现在在线玩家:"
                );
                for (Player p : GuildManager.getPlugin().getServer().getOnlinePlayers()) {
                    if (guild.hasPlayer(p.getName())) {
                        msg.append("§e").append(p.getName()).append(",");
                    }
                }
                player.sendMessage(msg.toString());
            }
            if (guild.isAdvancedPlayer(player.getName())) {
                player.sendMessage(Msg.INFO + "§2您是公会高级成员，可以前往公会广场");
                new Operation().giveGuildSquarePerm(playerID);
            } else {
                new Operation().removeGuildSquarePerm(playerID);
            }
            if (!guild.isMsgBoardEmpty()) {
                player.sendMessage("§2公会留言板为空");
            }
        } else {
            // 玩家无公会
            new Operation().removeGuildSquarePerm(playerID);
        }
    }
}
