package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GuildRepository implements Listener {
    GuildManager plugin = GuildManager.plugin;

    void openRepos (Player player, Guild guild) throws IOException {
        Inventory repos = newRepos(guild.getId(),guild.getReposSize());
        restoreRepos(guild.getId(),repos);
        player.openInventory(repos);
    }

    Inventory newRepos (String guildID , int size){
        Guild guild = plugin.guilds.getGuild(guildID);
        Inventory repos = Bukkit.createInventory(null, size, guild.getName()+" §6公会仓库");
        return repos;
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

    @EventHandler
    public void saveReposOnDisable(InventoryCloseEvent event) throws IOException {
        if(event.getInventory().getTitle().contains("公会仓库")) {
            String playerID = event.getPlayer().getName();
            Guild guild = plugin.guilds.getPlayersGuild(playerID);
            saveRepos(guild.getId(),event.getInventory());
        }
    }


    //save and load
    void saveRepos(String guildID,Inventory repos)throws IOException{
        File f =new File(plugin.getDataFolder().getAbsolutePath(),
                "/Repositories/"+guildID+".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set(guildID,repos.getStorageContents());
        c.save(f);
    }

    @SuppressWarnings("unchecked")
    void restoreRepos(String guildID,Inventory repos)throws IOException{
        File f =new File(plugin.getDataFolder().getAbsolutePath(),
                "/Repositories/"+guildID+".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        ItemStack[] content = ((List< ItemStack >)c.get(guildID)).toArray(new ItemStack[0]);
        repos.setStorageContents(content);
    }

}
