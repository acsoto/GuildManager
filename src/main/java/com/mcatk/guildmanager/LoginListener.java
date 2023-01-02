package com.mcatk.guildmanager;

import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.Member;
import com.mcatk.guildmanager.sql.SQLManager;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LoginListener implements Listener {

    @EventHandler
    public void onAuth(LoginEvent e) {
        Player player = e.getPlayer();
        Guild guild = SQLManager.getInstance().getPlayerGuild(player.getName());
        if (guild != null) {
            // 玩家有公会
            if (guild.getChairman().equals(player.getName())) {
                // 会长 全部公告
                GuildManager.getPlugin().getServer().broadcastMessage(
                        Msg.INFO + "§6" + guild.getGuildName() + " §7会长 §e" + player.getName() + " §7已上线"
                );
            } else {
                // 非会长 会内公告
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (SQLManager.getInstance().getGuildMembers(guild.getId()).contains(p.getName())) {
                        p.sendMessage(
                                Msg.INFO + "§6" + guild.getGuildName() + " §7成员 §e" + player.getName() + " §7已上线"
                        );
                    }
                }
            }
            Member member = SQLManager.getInstance().getMember(player.getName());
            if (member.isAdvanced()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "res pset main.gh " + player + " move true");
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "res pset main.gh " + player + " move remove");
            }
        } else {
            // 玩家无公会
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "res pset main.gh " + player + " move remove");
        }
    }
}
