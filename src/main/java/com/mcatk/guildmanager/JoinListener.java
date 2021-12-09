package com.mcatk.guildmanager;

import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.sql.SQLManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerID = player.getName();
        Guild guild = SQLManager.getInstance().getPlayerGuild(playerID);
        if (guild != null) {
            // 玩家有公会
            if (guild.getChairman().equals(playerID)) {
                GuildManager.getPlugin().getServer().broadcastMessage(
                        Msg.INFO + guild.getGuildName() + "§2会长§e" + player.getName() + "§2已上线"
                );
                new Operation().giveGuildSquarePerm(playerID);
            }
            if (GuildManager.getPlugin().getServer().getOnlinePlayers() != null) {
                StringBuilder msg = new StringBuilder(
                        Msg.INFO + "§2您的公会" + guild.getGuildName() + "§2现在在线玩家:"
                );
                for (Player p : GuildManager.getPlugin().getServer().getOnlinePlayers()) {
                    if (guild.getId().equals(SQLManager.getInstance().getPlayerGuild(p.getName()).getId())) {
                        msg.append("§e").append(p.getName()).append(",");
                    }
                }
                player.sendMessage(msg.toString());
            }
        } else {
            // 玩家无公会
            new Operation().removeGuildSquarePerm(playerID);
        }
    }
}
