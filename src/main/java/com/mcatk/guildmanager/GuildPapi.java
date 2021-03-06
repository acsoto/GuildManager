package com.mcatk.guildmanager;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

//The class is used for PAPI
public class GuildPapi extends PlaceholderExpansion {
    private final GuildManager plugin;
    
    public GuildPapi(GuildManager plugin) {
        this.plugin = plugin;
    }
    
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
        return plugin.getDescription().getAuthors().toString();
    }
    
    @Override
    public String getIdentifier() {
        return "guildmanager";
    }
    
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        
        if (player == null) {
            return "";
        }
        Guild guild = plugin.getGuilds().getPlayersGuild(player.getName());
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
            if (guild.hasLeader(player.getName())) {
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
            return Integer.toString(guild.getPlayersNum());
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