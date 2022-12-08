package com.mcatk.guildmanager;

import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.Member;
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
            Member member = SQLManager.getInstance().getMember(playerID);
            if (member.isAdvanced()) {
                new Operation().giveGuildSquarePerm(playerID);
            } else {
                new Operation().removeGuildSquarePerm(playerID);
            }
        } else {
            // 玩家无公会
            new Operation().removeGuildSquarePerm(playerID);
        }
    }
}
