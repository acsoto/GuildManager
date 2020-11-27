package com.mcatk.guildmanager;


import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

//The class is used for PAPI
public class GuildPAPI extends PlaceholderExpansion {

    private final GuildManager plugin;

    public GuildPAPI(GuildManager plugin){
        this.plugin = plugin;
    }

    //PAPI要求的重写
    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "guildmanager";
    }

    public String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }
        Guild guild = plugin.guilds.getPlayersGuild(player.getName());
        if(guild==null)
            return null;
        //玩家所在公会的变量
        if(identifier.equals("id")){
            return guild.getID();
        }
        if(identifier.equals("name")){
            return guild.getName();
        }
        if(identifier.equals("chairman")){
            return guild.getChairman();
        }
        if(identifier.equals("cash")){
            int cash = guild.getCash();
            return Integer.toString(cash);
        }
        if(identifier.equals("points")){
            int points = guild.getPoints();
            return Integer.toString(points);
        }
        if(identifier.equals("level")){
            int level = guild.getLevel();
            return Integer.toString(level);
        }
        if(identifier.equals("num_player")){
            int n = guild.getPlayersNum();
            return Integer.toString(n);
        }
        if(identifier.equals("max_player")){
            int n = guild.getMaxPlayers();
            return Integer.toString(n);
        }
        if(identifier.equals("num_advanced_player")){
            int n = guild.getAdvancedPlayersNum();
            return Integer.toString(n);
        }
        if(identifier.equals("max_advanced_player")){
            int n = guild.getMaxAdvancedPlayers();
            return Integer.toString(n);
        }
        //玩家的相关变量
        Member member = guild.getMember(player.getName());
        if(identifier.equals("isadvanced")){
            if(member.isAdvanced())
                return "是";
            else return "否";
        }
        if (identifier.equals("contribution")){
            int contribution = member.getContribution();
            return Integer.toString(contribution);
        }
        if(identifier.equals("position")){
            String playerID = player.getName();
            if(guild.getChairman().equals(playerID))
                return "会长";
            else if(guild.hasViceChairman(playerID))
                return "副会长";
            else if(guild.hasManager(playerID))
                return "管理员";
            else return "无";
        }



        return null;
    }
}