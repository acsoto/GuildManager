package com.mcatk.guildmanager;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class Guilds {
    private GuildManager plugin;
    private HashMap<String, Guild> guildMap;
    
    public Guilds(GuildManager plugin) {
        this.plugin = plugin;
        loadFromConfig();
    }
    
    public void loadFromConfig() {
        guildMap = new HashMap<>();
        plugin.getLogger().info("加载公会列表...");
        ConfigurationSection configGuilds =
                plugin.getConfig().getConfigurationSection("Guilds");
        for (String key : configGuilds.getKeys(false)) {
            Guild g = (Guild) configGuilds.get(key);
            guildMap.put(key, g);
            plugin.getLogger().info("成功载入公会" + g.getName());
        }
        plugin.getLogger().info("成功载入公会列表");
    }
    
    public void saveToConfig() {
        plugin.getLogger().info("保存公会列表...");
        ConfigurationSection configGuilds =
                plugin.getConfig().getConfigurationSection("Guilds");
        for (Guild guild : guildMap.values()) {
            configGuilds.set(guild.getId(), guild);
        }
        plugin.saveConfig();
        plugin.getLogger().info("成功保存公会列表");
    }
    
    //新建公会，加入map并且存入config
    void newGuild(String id, String player) {
        Guild g = new Guild(id, player);
        guildMap.put(id, g);
        saveToConfig();
    }
    
    public HashMap<String, Guild> getGuildMap() {
        return guildMap;
    }
    
    //删除公会，从map删除并且从config删除
    public boolean removeGuild(String id) {
        if (guildMap.remove(id) != null) {
            plugin.getConfig().set("Guilds." + id, null);
            return true;
        }
        return false;
    }
    
    //查公会
    Boolean hasGuild(String id) {
        return guildMap.containsKey(id);
    }
    
    Guild getGuild(String id) {
        if (hasGuild(id)) {
            return guildMap.get(id);
        }
        return null;
    }
    
    Guild getPlayersGuild(String p) {
        for (String key : guildMap.keySet()) {
            Guild g = guildMap.get(key);
            if (g.hasPlayer(p)) {
                return g;
            }
        }
        return null;
    }
    
    boolean isPlayerInAnyApplicantList(String p) {
        for (String key :
                guildMap.keySet()) {
            Guild g = getGuild(key);
            if (g.getApplicantList().contains(p)) {
                return true;
            }
        }
        return false;
    }
}
