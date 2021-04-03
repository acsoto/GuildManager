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
import java.util.HashMap;

public class GuildGui implements Listener {
    
    private GuildManager plugin;
    private Guilds guilds;
    
    //公会列表GUI
    public GuildGui(GuildManager plugin) {
        this.plugin = plugin;
        this.guilds = plugin.getGuilds();
    }
    
    public void openGui(Player player) {
        player.openInventory(createGuildsGui());
    }
    
    public ItemStack getAnGuildButton(Guild guild) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(guild.getChairman());
        item.setItemMeta(meta);
        meta.setDisplayName(guild.getName());
        ArrayList<String> des = new ArrayList<>();
        des.add("§2公会ID: §a" + guild.getId());
        des.add("§2会长: §a" + guild.getChairman());
        des.add(guild.checkViceChairman());
        des.add(guild.checkManager());
        des.add("§2成员: §a" + guild.getPlayerSize() + "§7/§2" + guild.getMaxPlayers());
        des.add("§2最大高级成员数: §a" + guild.getMaxAdvancedPlayers());
        des.add("§2等级: §a" + guild.getLevel());
        des.add("§2积分: §a" + guild.getPoints());
        des.add("§2资金: §a" + guild.getCash());
        meta.setLore(des);
        item.setItemMeta(meta);
        return item;
    }
    
    public Inventory createGuildsGui() {
        Inventory gui = Bukkit.createInventory(null, 54, "§6公会列表");
        for (String key :
                guilds.getGuildMap().keySet()) {
            Guild guild = guilds.getGuild(key);
            ItemStack button = getAnGuildButton(guild);
            gui.addItem(button);
        }
        gui.setItem(53,getQuitIcon());
        return gui;
    }
    
    
    //成员GUI
    
    public void openMemGui(Player player, Guild guild) {
        player.openInventory(createGuildMemGui(guild));
    }
    
    public Inventory createGuildMemGui(Guild guild) {
        HashMap<String, Member> memList = guild.getMembers();
        Inventory gui = Bukkit.createInventory(null, 54, guild.getName() + "§6成员");
        for (String playerID :
                memList.keySet()) {
            Member player = guild.getMember(playerID);
            ItemStack button = getAnMemberButton(guild, player);
            gui.addItem(button);
        }
        return gui;
    }
    
    public ItemStack getAnMemberButton(Guild guild, Member player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(player.getId());
        ArrayList<String> des = new ArrayList<>();
        if (guild.hasLeader(player.getId())) {
            String playerName = player.getId();
            if (guild.getChairman().equalsIgnoreCase(playerName)) {
                des.add("§6会长");
            } else if (guild.getViceChairman().contains(playerName)) {
                des.add("§6副会长");
            } else if (guild.getManager().contains(playerName)) {
                des.add("§6管理员");
            }
        }
        des.add("§2ID: §a" + player.getId());
        des.add("§2贡献度: §a" + player.getContribution());
        meta.setLore(des);
        item.setItemMeta(meta);
        return item;
    }
    
    //留言板GUI
    public void openMsgGui(Player player, Guild guild) {
        player.openInventory(createGuildMsgGui(guild));
    }
    
    public Inventory createGuildMsgGui(Guild guild) {
        ArrayList<String> msgBoard = guild.getMsgBoard();
        Inventory gui = Bukkit.createInventory(null, 18, guild.getName() + "§6留言板");
        for (String str :
                msgBoard) {
            ItemStack button = getAnMsgButton(str);
            gui.addItem(button);
        }
        return gui;
    }
    
    public ItemStack getAnMsgButton(String str) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(str);
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack getQuitIcon(){
        ItemStack icon =  new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("返回");
        icon.setItemMeta(meta);
        return icon;
    }
    
    
    //禁止玩家拿走物品
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        boolean unClickableGui =
                event.getInventory().getTitle().equalsIgnoreCase("§6公会列表") ||
                        event.getInventory().getTitle().contains("成员");
        if (unClickableGui) {
            event.setCancelled(true);
            //            player.updateInventory();
            //            if(event.getRawSlot()==0){
            //                player.closeInventory();
            //                player.openInventory(createGuildsGUI());
            //            }
        }
    }
    
}
