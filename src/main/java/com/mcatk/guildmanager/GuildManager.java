package com.mcatk.guildmanager;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;


import java.util.HashMap;

public final class GuildManager extends JavaPlugin {

    public static GuildManager plugin;
    private final HashMap<String , Guild> GuildList=new HashMap<>();
    private static Economy econ=null;
    private int reqCreateGuildMoney;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().warning("未找到前置插件Vault，即将关闭插件");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("检测到Vault，成功启动GuildManager");
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null){
            getLogger().info("检测到PlaceholderAPI，已启动PAPI变量");
            new GuildPAPI(this).register();
        }
        //实例化
        plugin=this;
        //生成配置文件
        saveDefaultConfig();
        getLogger().info("公会管理插件已启动-soto");
        //注册指令
        Bukkit.getPluginCommand("gmg").setExecutor(new GuildCommand());
        Bukkit.getPluginCommand("gmgadmin").setExecutor(new GuildAdmin());
        //注册序列化
        ConfigurationSerialization.registerClass(Member.class);
        ConfigurationSerialization.registerClass(Guild.class);
        //注册监听器
        Bukkit.getPluginManager().registerEvents(new JoinListener(),this);
        //读取配置文件
        if(!getConfig().contains("CreateGuildMoney")){
            getLogger().warning("配置文件错误，即将关闭插件，请删除配置文件后重试");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        reqCreateGuildMoney =
                (int) getConfig().get("CreateGuildMoney");
        //读取公会列表
        if(getConfig().contains("Guilds")) {
            loadGuildList();
        }
        else {
            getLogger().info("公会列表为空");
        }
    }
    @Override
    public void onDisable() {
        getLogger().info("公会管理插件已关闭-soto");
        saveConfig();
    }

    void reloadPlugin(){
        reqCreateGuildMoney =
                (int) getConfig().get("CreateGuildMoney");
        GuildList.clear();
        loadGuildList();
    }

    int getReqCreateGuildMoney(){
        return reqCreateGuildMoney;
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
    void newGuild(String ID){
        Guild g = new Guild(ID);
        GuildList.put(ID,g);
        getConfig().set("Guilds."+ID,g);
        saveConfig();
    }
    void newGuild(String ID, String player){
        Guild g = new Guild(ID,player);
        GuildList.put(ID,g);
        getConfig().set("Guilds."+ID,g);
        saveConfig();
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
        sendConsoleCmd("warp "+g+" "+p);
    }
    void setWarp(Player player , String g){
        player.setOp(true);
        player.chat("/setwarp Guild_"+g);
        player.setOp(false);

    }
    void delWarp(String g){
        sendConsoleCmd("/delwarp Guild_"+g);
    }

    void loadGuildList(){
        ConfigurationSection configGuilds =
                getConfig().getConfigurationSection("Guilds");
        for(String key : configGuilds.getKeys(false)){
            Guild g = (Guild) configGuilds.get(key);
            GuildList.put(key,g);
            g.resetRemoveMemLimitFlag();
            getLogger().info("成功载入公会"+g.getName());
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
    //log
    void logInfo(String s){
        getLogger().info(s);
    }
    void logWarn(String s){
        getLogger().warning(s);
    }
    //console command
    void sendConsoleCmd(String cmd){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
    }
    //color
    String colorFormat (String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
