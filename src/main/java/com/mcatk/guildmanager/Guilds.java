package com.mcatk.guildmanager;

import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Guilds {
    GuildManager plugin = GuildManager.plugin;

    private final HashMap<String , Guild> GuildList=new HashMap<>();

    Guild getChairmansGuild(String player){
        for(String i:GuildList.keySet()){
            Guild g= GuildList.get(i);
            if(g.getChairman().equalsIgnoreCase(player)){
                return g;
            }
        }
        return null;
    }

    void listGuilds(CommandSender sender){
        sender.sendMessage("§a------------公会列表------------");
        for(String i:GuildList.keySet()){
            Guild g = GuildList.get(i);
            sender.sendMessage("§2"+i+" §a公会名: "+g.getName()+" §e会长: "+g.getChairman());
        }
    }
    //新建公会，加入map并且存入config
    void newGuild(String ID, String player){
        Guild g = new Guild(ID,player);
        GuildList.put(ID,g);
        plugin.getConfig().set("Guilds."+ID,g);
        plugin.saveConfig();
    }
    void addGuild(String key,Guild guild){
        GuildList.put(key, guild);
    }

    public HashMap<String, Guild> getGuildList() {
        return GuildList;
    }

    //删除公会，从map删除并且从config删除
    Boolean removeGuild(String ID){
        if(GuildList.remove(ID)!=null){
            plugin.getConfig().set("Guilds."+ID,null);
            return true;
        }
        return false;
    }
    //查公会
    Boolean hasGuild(String ID){
        return GuildList.containsKey(ID);
    }
    Guild getGuild(String ID){
        if(hasGuild(ID))
            return GuildList.get(ID);
        return null;
    }

    Guild getPlayersGuild(String p){
        for(String key:GuildList.keySet()){
            Guild g = GuildList.get(key);
            if(g.hasPlayer(p))
                return g;
        }
        return null;
    }
    void clearGuildList(){
        GuildList.clear();
    }
    //0:无同盟 1:成功 2:资金不足
    int donateAlly(Guild guild, int n){
        Guild allyGuild = getGuild(guild.getAlly());
        if(allyGuild==null){
            return 0;
        }
        if(guild.getCash()>=n){
            guild.takeCash(n);
            allyGuild.addCash(n);
            return 1;
        }
        return 2;
    }

    boolean isPlayerInAnyApplicantList(String p){
        for (String key :
                GuildList.keySet()) {
            Guild  g = getGuild(key);
            if (g.getApplicantList().contains(p))
                return true;
        }
        return false;
    }
}
