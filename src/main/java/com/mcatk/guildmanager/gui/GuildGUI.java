package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuildGUI implements Listener {

    private final Inventory gui;
    private ItemStack back;
    private ItemStack tp;

    private final Guild guild;


    public GuildGUI(Guild guild) {
        Bukkit.getPluginManager().registerEvents(this, GuildManager.getPlugin());
        this.guild = guild;
        gui = getGui();
    }

    public void openGUI(Player player) {
        player.openInventory(gui);
    }

    private Inventory getGui() {
        Inventory gui = Bukkit.createInventory(null, 9, guild.getGuildName());
        gui.setItem(0, tp = getTpIcon());
        gui.setItem(1, getMemberIcon());
        gui.setItem(8, back = getQuitIcon());
        return gui;
    }

    private ItemStack getTpIcon() {
        ItemStack icon = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("§9传送到公会领地");
        icon.setItemMeta(meta);
        return icon;
    }

    private ItemStack getMemberIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_HELMET);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("§9成员列表");
        ArrayList<String> members = new ArrayList<>();
        for (String member : SQLManager.getInstance().getGuildMembers(guild.getId())) {
            members.add("§e" + member);
        }
        meta.setLore(members);
        icon.setItemMeta(meta);
        return icon;
    }

    private ItemStack getQuitIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("§e返回");
        icon.setItemMeta(meta);
        return icon;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().equals(gui)) return;
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        if (clickedItem.equals(back)) {
            new GuildsGUI().openGUI((Player) e.getWhoClicked());
            return;
        }
        if (clickedItem.equals(tp)) {
            ((Player) e.getWhoClicked()).chat(String.format("/warp %s", guild.getGuildName()));
        }
    }
}
