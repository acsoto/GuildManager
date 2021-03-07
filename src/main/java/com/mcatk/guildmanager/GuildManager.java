package com.mcatk.guildmanager;

import com.mcatk.guildmanager.command.GuildAdmin;
import com.mcatk.guildmanager.command.GuildCommand;
import com.mcatk.guildmanager.command.GuildCommandS;
import com.mcatk.guildmanager.file.FileOperation;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        saveDefaultConfig();
        registerDependency();
        guilds = FileOperation.loadGuilds();
        if (guilds == null) {
            guilds = new Guilds();
        }
        registerCommand();
        registerListener();
        getLogger().info("公会管理插件已启动-soto");
    }
    
    @Override
    public void onDisable() {
        FileOperation.saveGuilds();
        getLogger().info("公会管理插件已关闭-soto");
    }
    
    private void registerDependency() {
        //检测前置插件
        if (!setupEconomy()) {
            getLogger().warning("未找到前置插件Vault，即将关闭插件");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("检测到Vault，成功启动依赖");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new GuildPapi(this).register();
            getLogger().info("检测到PlaceholderAPI，已启动PAPI变量");
        }
    }
    
    private void registerCommand() {
        Bukkit.getPluginCommand("gmg").
                setExecutor(new GuildCommand(plugin));
        Bukkit.getPluginCommand("gmgs").
                setExecutor(new GuildCommandS(plugin));
        Bukkit.getPluginCommand("gmgadmin").
                setExecutor(new GuildAdmin(plugin));
        getLogger().info("注册指令注册完毕");
    }
    
    private void registerListener() {
        Bukkit.getPluginManager().
                registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().
                registerEvents(new GuildGui(this), this);
        Bukkit.getPluginManager().
                registerEvents(new GuildItem(this), this);
        Bukkit.getPluginManager().
                registerEvents(new GuildRepository(this), this);
        getLogger().info("监听器注册完毕");
    }
    
    //公会传送指令
    public void tpGuild(String g, String p) {
        sendConsoleCmd("warp Guild_" + g + " " + p);
    }
    
    public void setWarp(Player player, String g) {
        player.setOp(true);
        player.chat("/setwarp Guild_" + g);
        player.setOp(false);
    }
    
    public void delWarp(String g) {
        sendConsoleCmd("/delwarp Guild_" + g);
    }
    
    public void tpAll(Guild guild, Player player) {
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
    public boolean takePlayerMoney(Player p, double m) {
        EconomyResponse r = econ.withdrawPlayer(p, m);
        return r.transactionSuccess();
    }
    
    //log
    public void logInfo(String s) {
        getLogger().info(s);
    }
    
    //console command
    public void sendConsoleCmd(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
    
    //color
    public String colorFormat(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    
}
