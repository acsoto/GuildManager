package com.mcatk.guildmanager.papi;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.Member;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

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
    public String getAuthor() {
        return GuildManager.getPlugin().getDescription().getAuthors().toString();
    }
    
    @Override
    public String getIdentifier() {
        return "guildmanager";
    }
    
    @Override
    public String getVersion() {
        return GuildManager.getPlugin().getDescription().getVersion();
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        
        if (player == null) {
            return "";
        }
        Guild guild = GuildManager.getPlugin().getGuilds().getPlayersGuild(player.getName());
        if (guild == null) {
            return "";
        }
        //玩家所在公会的变量
        if (identifier.equals("id")) {
            return guild.getId();
        }
        if (identifier.equals("name")) {
            return guild.getName();
        }
        if (identifier.equals("prefix")) {
            if (guild.hasChairman(player.getName())) {
                return "";
            } else {
                return "§7[" + guild.getName() + "§7]&r";
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
            return Integer.toString(guild.getPlayerSize());
        }
        if (identifier.equals("max_player")) {
            return Integer.toString(guild.getMaxPlayers());
        }
        if (identifier.equals("max_advanced_player")) {
            return Integer.toString(guild.getMaxAdvancedPlayers());
        }
        //玩家的相关变量
        Member member = guild.getMember(player.getName());
        if (identifier.equals("contribution")) {
            return Integer.toString(member.getContribution());
        }
        if (identifier.equals("position")) {
            return position(guild, player);
        }
        if (identifier.equals("position_prefix")) {
            return positionPrefix(guild, player);
        }
        return null;
    }
    
    private String position(Guild guild, Player player) {
        String playerID = player.getName();
        if (guild.getChairman().equals(playerID)) {
            return "会长";
        } else if (guild.hasViceChairman(playerID)) {
            return "副会长";
        } else if (guild.hasManager(playerID)) {
            return "管理员";
        } else {
            return "无";
        }
    }
    
    private String positionPrefix(Guild guild, Player player) {
        String playerID = player.getName();
        if (guild.getChairman().equals(playerID)) {
            return "§7[" + guild.getName() + "§f|§4" + "会长" + "§7]&r";
        } else if (guild.hasViceChairman(playerID)) {
            return "§7[" + guild.getName() + "§f|§c" + "副会长" + "§7]&r";
        } else if (guild.hasManager(playerID)) {
            return "";
        } else {
            return "";
        }
    }
}