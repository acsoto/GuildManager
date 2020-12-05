package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class GuildRepository implements Listener {
    HashMap<String, Inventory> guildRepositories;
    GuildManager plugin = GuildManager.plugin;

    void newRepos (String guildID){
        Guild guild = plugin.guilds.getGuild(guildID);
        Inventory repos = Bukkit.createInventory(null, 9, guild.getName()+" §6公会仓库");
        guildRepositories.put(guildID,repos);
    }

    void setReposSize(String guildID , int size){
        Guild guild = plugin.guilds.getGuild(guildID);
        guildRepositories.remove(guildID);
        Inventory repos = Bukkit.createInventory(null, size, guild.getName()+" §6公会仓库");
        guildRepositories.put(guildID,repos);
    }

    int levelUpRepos(String guildID){
        Inventory repos = guildRepositories.get(guildID);
        int reposSize = repos.getSize();
        if(reposSize==54)
            return 0;
        if(repos.getContents()!=null)
            return 1;
        reposSize+=9;
        setReposSize(guildID,reposSize);
        return 2;
    }

    //公会仓库规则：贡献度大于10或者leader可以存取
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }
        if(event.getInventory().getTitle().contains("公会仓库")) {
            Player player = (Player) event.getWhoClicked();
            String playerID = player.getName();
            Guild guild = plugin.guilds.getPlayersGuild(playerID);
            if (guild.isLeader(playerID) ||
                    guild.getMember(playerID).getContribution() > 10) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }

}
