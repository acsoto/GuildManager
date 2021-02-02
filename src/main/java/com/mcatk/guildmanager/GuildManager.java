package com.mcatk.guildmanager;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;



public final class GuildManager extends JavaPlugin {

    public static GuildManager plugin;
    public Guilds guilds;
    public GuildGUI gui;
    public GuildRepository guildRepository;
    public JoinListener joinListener;
    public GuildItem guildItem;
    public PlayerCache playerCache;
    private static Economy econ;
    private int reqCreateGuildMoney;


    @Override
    public void onEnable() {
        //检测前置插件
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
        getLogger().info("初始化...");
        plugin = this;
        guilds = new Guilds();
        gui = new GuildGUI();
        guildItem = new GuildItem();
        guildRepository = new GuildRepository();
        joinListener = new JoinListener(plugin,guilds);
        playerCache = new PlayerCache();
        //生成配置文件
        saveDefaultConfig();
        //注册指令
        Bukkit.getPluginCommand("gmg").
                setExecutor(new GuildCommand(plugin,guilds));
        Bukkit.getPluginCommand("gmgs").
                setExecutor(new GuildCommandS(plugin,guilds));
        Bukkit.getPluginCommand("gmgadmin").
                setExecutor(new GuildAdmin(plugin,guilds));
        getLogger().info("成功注册指令");
        //注册序列化
        ConfigurationSerialization.registerClass(Member.class);
        ConfigurationSerialization.registerClass(Guild.class);
        getLogger().info("成功注册序列化");
        //注册监听器
        Bukkit.getPluginManager().
                registerEvents(joinListener,this);
        Bukkit.getPluginManager().
                registerEvents(gui,this);
        Bukkit.getPluginManager().
                registerEvents(guildItem,this);
        Bukkit.getPluginManager().
                registerEvents(guildRepository,this);
        getLogger().info("成功注册监听器");
        //读取配置文件
        if(!getConfig().contains("CreateGuildMoney")){
            getLogger().warning("配置文件错误，即将关闭插件，请删除配置文件后重试");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        reqCreateGuildMoney =
                (int) getConfig().get("CreateGuildMoney");
        getLogger().info("配置文件读取完毕");
        //读取公会列表
        getLogger().info("加载公会...");
        if(getConfig().contains("Guilds")) {
            loadGuildList();
            gui.createGuildsGUI();
        }
        else {
            getLogger().info("公会列表为空");
        }
        getLogger().info("公会管理插件已启动-soto");
    }
    @Override
    public void onDisable() {
        getLogger().info("公会管理插件已关闭-soto");
        saveConfig();
    }

    void reloadPlugin(){
        reqCreateGuildMoney =
                (int) getConfig().get("CreateGuildMoney");
        guilds.clearGuildList();
        loadGuildList();
    }


    int getReqCreateGuildMoney(){
        return reqCreateGuildMoney;
    }


    //公会传送指令
    void tpGuild(String g, String p){
        sendConsoleCmd("warp Guild_"+g+" "+p);
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
            guilds.addGuild(key,g);
            g.resetRemoveMemLimitFlag();
            getLogger().info("成功载入公会"+colorFormat(g.getName()));
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
