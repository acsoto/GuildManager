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
        Guild guild = plugin.getPlayersGuild(player.getName());
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



        return null;
    }
}