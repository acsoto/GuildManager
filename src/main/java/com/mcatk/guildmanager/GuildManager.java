package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;


public final class GuildManager extends JavaPlugin {

    public static GuildManager plugin;
    static HashMap<String , Guild> GuildList=new HashMap<>();
    @Override
    public void onEnable() {
        //实例化
        plugin=this;
        //生成配置文件
        saveDefaultConfig();
        this.getLogger().info("§a公会管理插件已启动-soto");
        //注册指令和序列化
        Bukkit.getPluginCommand("gmg").setExecutor(new GuildCommand());
        Bukkit.getPluginCommand("gmgadmin").setExecutor(new GuildAdmin());
        ConfigurationSerialization.registerClass(Guild.class);
        //读取公会列表
        if(getConfig().contains("Guilds"))
            loadGuildList();
        else this.getLogger().info("§a公会列表为空");
    }
    @Override
    public void onDisable() {
        this.getLogger().info("§a公会管理插件已关闭-soto");
        saveConfig();
    }


    Guild getPlayersGuild(String player){
        for(String i:GuildList.keySet()){
            Guild g= GuildList.get(i);
            if(g.ChairMan.equalsIgnoreCase(player)){
                return g;
            }
        }
        return null;
    }

    void listGuilds(CommandSender sender){
        sender.sendMessage("§a------------公会列表------------");
        for(String i:GuildList.keySet()){
            Guild g = GuildList.get(i);
            sender.sendMessage("§2"+i+" §a公会名: "+g.GuildName+" §e会长: "+g.ChairMan);
        }
    }
    //新建公会，加入map并且存入config
    Guild newGuild(String ID){
        Guild g = new Guild(ID);
        GuildList.put(ID,g);
        getConfig().set("Guilds."+ID,g);
        saveConfig();
        return g;
    }
    //删除公会，从map删除并且从config删除
    Boolean removeGuild(String ID){
        if(GuildList.remove(ID)!=null){
            getConfig().set("Guilds."+ID,null);
            return true;
        }
        return false;
    }
    //查公会
    Boolean hasGuild(String ID){
        if(GuildList.containsKey(ID))
            return true;
        return false;
    }
    Guild getGuild(String ID){
        if(hasGuild(ID))
            return GuildList.get(ID);
        return null;
    }

    void setChairman(String ID,String p){
        Guild g=GuildList.get(ID);
        g.setChairman(p);
    }
    Guild PlayersGuild(String p){
        for(String key:GuildList.keySet()){
            Guild g = GuildList.get(key);
            if(g.hasPlayer(p))
                return g;
        }
        return null;
    }
    //公会传送指令
    Boolean tpGuild(String g, String p){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"warp "+g+" "+p);
        return true;
    }
    void loadGuildList(){
        ConfigurationSection config = getConfig().getConfigurationSection("Guilds");
        for(String key : config.getKeys(false)){
            Guild g = (Guild) config.get(key);
            GuildList.put(key,g);
            g.setRemoveMemLimitFlag(0);
            this.getLogger().info("§a成功载入公会"+g.getName());
        }
        saveConfig();
    }

}
