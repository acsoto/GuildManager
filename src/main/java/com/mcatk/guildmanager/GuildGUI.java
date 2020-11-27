package com.mcatk.guildmanager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class GuildGUI implements Listener {

    GuildManager plugin = GuildManager.plugin;
    Guilds guilds = plugin.guilds;
    Inventory guildsGUI ;


    public GuildGUI() {
        guildsGUI = Bukkit.createInventory(null, 54, "§6公会列表");
    }


    ItemStack getAnGuildButton(Guild guild) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta =  item.getItemMeta();
        meta.setDisplayName(guild.getName());
        ArrayList<String> des = new ArrayList<>();
        des.add("§2公会ID: §a" + guild.getID());
        des.add("§2会长: §a" + guild.getChairman());
        des.add(guild.checkViceChairman());
        des.add(guild.checkManager());
        des.add("§2成员: §a" + guild.getPlayersNum() + "§7/§2" + guild.getMaxPlayers());
        des.add("§2高级成员: §a" + guild.getAdvancedPlayersNum() + "§7/§2" + guild.getMaxAdvancedPlayers());
        des.add("§2等级: §a" + guild.getLevel());
        des.add("§2积分: §a" + guild.getPoints());
        des.add("§2资金: §a" + guild.getCash());
        des.add("§2伙伴公会: §a" + guild.getAlly());
        des.add("§2联盟: §a" + guild.getLeague());
        meta.setLore(des);
        item.setItemMeta(meta);
        return item;
    }

    void reloadButtons(){
        guildsGUI.clear();
        createGuildsGUI();
    }

    void openGUI(Player player){
        player.openInventory(guildsGUI);
    }

    void createGuildsGUI(){
        for (String key :
                guilds.getGuildList().keySet()) {
            Guild guild = guilds.getGuild(key);
            ItemStack button = getAnGuildButton(guild);
            guildsGUI.addItem(button);
        }
    }

    //禁止玩家拿走物品
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if(event.getInventory().getTitle().equalsIgnoreCase("§6公会列表")){
            event.setCancelled(true);
            player.updateInventory();
            if(event.getRawSlot()==0){
                player.closeInventory();
                player.openInventory(guildsGUI);
            }
        }
    }

}
