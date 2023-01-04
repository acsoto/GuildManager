package com.mcatk.guildmanager.papi;

import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.GuildBasicInfo;
import com.mcatk.guildmanager.models.Member;
import com.mcatk.guildmanager.sql.SQLManager;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

//The class is used for PAPI
public class GuildPapi extends PlaceholderExpansion {

    //PAPI要求的重写
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return GuildManager.getPlugin().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "guildmanager";
    }

    @Override
    public @NotNull String getVersion() {
        return GuildManager.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }
        Guild guild = SQLManager.getInstance().getPlayerGuild(player.getName());
        if (guild == null) {
            return "";
        }
        //玩家所在公会的变量
        if (identifier.equals("id")) {
            return guild.getId();
        }
        if (identifier.equals("name")) {
            return guild.getGuildName();
        }
        if (identifier.equals("prefix")) {
            if (guild.getChairman().equals(player.getName())) {
                return "§7[" + guild.getGuildName() + "§f|§4" + "会长" + "§7]&r";
            } else if (guild.getViceChairman1().equals(player.getName()) || guild.getViceChairman2().equals(player.getName())) {
                return "§7[" + guild.getGuildName() + "§f|§c" + "副会长" + "§7]&r";
            } else {
                return "§7[" + guild.getGuildName() + "§7]&r";
            }
        }
        if (identifier.equals("chairman")) {
            return guild.getChairman();
        }
        if (identifier.equals("cash")) {
            return Integer.toString(guild.getCash());
        }
        if (identifier.equals("points")) {
            return Integer.toString(guild.getPoints());
        }
        if (identifier.equals("level")) {
            return Integer.toString(guild.getLevel());
        }
        if (identifier.equals("num_player")) {
            return Integer.toString(SQLManager.getInstance().getGuildMembers(guild.getId()).size());
        }
        if (identifier.equals("max_player")) {
            return Integer.toString(GuildBasicInfo.getMaxPlayer(guild.getLevel()));
        }
        if (identifier.equals("max_advanced_player")) {
            return Integer.toString(GuildBasicInfo.getMaxAdvancedPlayer(guild.getLevel()));
        }
        //玩家的相关变量
        Member member = SQLManager.getInstance().getMember(player.getName());
        if (identifier.equals("contribution")) {
            return Integer.toString(member.getContribution());
        }
        return null;
    }
}