package com.mcatk.guildmanager;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class GuildManager extends JavaPlugin {
    
    private static GuildManager plugin;
    private Guilds guilds;
    private static Economy econ;
    
    public static GuildManager getPlugin() {
        return plugin;
    }
    
    public Guilds getGuilds() {
        return guilds;
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        //检测前置插件
        if (!setupEconomy()) {
            getLogger().warning("未找到前置插件Vault，即将关闭插件");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("检测到Vault，成功启动GuildManager");
        //生成配置文件
        saveDefaultConfig();
        //实例化
        getLogger().info("初始化...");
        guilds = new Guilds(plugin);
        //注册指令
        Bukkit.getPluginCommand("gmg").
                setExecutor(new GuildCommand(plugin));
        Bukkit.getPluginCommand("gmgs").
                setExecutor(new GuildCommandS(plugin));
        Bukkit.getPluginCommand("gmgadmin").
                setExecutor(new GuildAdmin(plugin));
        getLogger().info("成功注册指令");
        //注册序列化
        ConfigurationSerialization.registerClass(Member.class);
        ConfigurationSerialization.registerClass(Guild.class);
        getLogger().info("成功注册序列化");
        //注册监听器
        Bukkit.getPluginManager().
                registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().
                registerEvents(new GuildGui(this), this);
        Bukkit.getPluginManager().
                registerEvents(new GuildItem(this), this);
        Bukkit.getPluginManager().
                registerEvents(new GuildRepository(this), this);
        getLogger().info("成功注册监听器");
        getLogger().info("公会管理插件已启动-soto");
    }
    
    @Override
    public void onDisable() {
        guilds.saveToConfig();
        getLogger().info("公会管理插件已关闭-soto");
    }
    
    void reloadPlugin() {
        guilds.loadFromConfig();
    }
    
    //公会传送指令
    void tpGuild(String g, String p) {
        sendConsoleCmd("warp Guild_" + g + " " + p);
    }
    
    void setWarp(Player player, String g) {
        player.setOp(true);
        player.chat("/setwarp Guild_" + g);
        player.setOp(false);
    }
    
    void delWarp(String g) {
        sendConsoleCmd("/delwarp Guild_" + g);
    }
    
    void tpAll(Guild guild, Player player) {
        for (Player p :
                getServer().getOnlinePlayers()) {
            String playerName = p.getName();
            if (guild.hasPlayer(playerName)) {
                player.chat("/tpahere " + playerName);
            }
        }
    }
    
    //启动Vault依赖
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().
                        getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    //经济操作
    Boolean takePlayerMoney(Player p, double m) {
        EconomyResponse r = econ.withdrawPlayer(p, m);
        return r.transactionSuccess();
    }
    
    //log
    void logInfo(String s) {
        getLogger().info(s);
    }
    
    
    //console command
    void sendConsoleCmd(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
    
    //color
    String colorFormat(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    
}
