package com.mcatk.guildmanager;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;


import java.util.HashMap;
import java.util.logging.Logger;

public final class GuildManager extends JavaPlugin {

    public static GuildManager plugin;
    private static final HashMap<String , Guild> GuildList=new HashMap<>();
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ=null;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - 未找到前置插件Vault", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //实例化
        plugin=this;
        //生成配置文件
        saveDefaultConfig();
        getLogger().info("§a公会管理插件已启动-soto");
        //注册指令
        Bukkit.getPluginCommand("gmg").setExecutor(new GuildCommand());
        Bukkit.getPluginCommand("gmgadmin").setExecutor(new GuildAdmin());
        //注册序列化
        ConfigurationSerialization.registerClass(Member.class);
        ConfigurationSerialization.registerClass(Guild.class);
        //注册监听器
        Bukkit.getPluginManager().registerEvents(new JoinListener(),this);
        //读取公会列表
        if(getConfig().contains("Guilds")) {
            loadGuildList();
        }
        else {
            getLogger().info("§a公会列表为空");
            Guild g = newGuild("ra");
            removeGuild("ra");
            saveConfig();
        }
    }
    @Override
    public void onDisable() {
        getLogger().info("§a公会管理插件已关闭-soto");
        saveConfig();
    }

    void reloadPlugin(){
        GuildList.clear();
        if(getConfig().contains("Guilds"))
            loadGuildList();
        else this.getLogger().info("§a公会列表为空");
    }


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
    Guild newGuild(String ID){
        Guild g = new Guild(ID);
        GuildList.put(ID,g);
        getConfig().set("Guilds."+ID,g);
        saveConfig();
        return g;
    }
    Guild newGuild(String ID, String player){
        Guild g = new Guild(ID,player);
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
    //公会传送指令
    void tpGuild(String g, String p){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"warp "+g+" "+p);
    }
    void setWarp(Player player , String g){
        player.setOp(true);
        player.chat("/setwarp Guild_"+g);
        player.setOp(false);

    }
    void delWarp(String g){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"/delwarp Guild_"+g);
    }

    void loadGuildList(){
        ConfigurationSection configGuilds =
                getConfig().getConfigurationSection("Guilds");
        for(String key : configGuilds.getKeys(false)){
            Guild g = (Guild) configGuilds.get(key);
            GuildList.put(key,g);
            g.resetRemoveMemLimitFlag();
            this.getLogger().info("§a成功载入公会"+g.getName());
        }
    }

    void tpAll(Guild guild,Player player){
        for (Player p :
                getServer().getOnlinePlayers()) {
            String pName = p.getName();
            if(guild.hasPlayer(pName)){
                player.chat("/tpahere "+pName);
            }
        }
    }
    //启动Vault依赖
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    //经济操作
    Boolean takePlayerMoney(Player p,double m){
        EconomyResponse r = econ.withdrawPlayer(p,m);
        return r.transactionSuccess();
    }

}
