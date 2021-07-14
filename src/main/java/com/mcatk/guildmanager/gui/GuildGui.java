package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.Guild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuildGui {
    
    private Guild guild;
    
    public GuildGui(Guild guild) {
        this.guild = guild;
    }
    
    public Inventory getGui() {
        Inventory gui = Bukkit.createInventory(GuiHolder.getGuiHolder(), 9, guild.toString());
        gui.addItem(getTpIcon());
        gui.addItem(getMemberIcon());
        return gui;
    }
    
    private ItemStack getTpIcon() {
        ItemStack icon = new ItemStack(Material.BED);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("§5点击传送到公会");
        icon.setItemMeta(meta);
        return icon;
    }
    
    private ItemStack getMemberIcon() {
        ItemStack icon = new ItemStack(Material.SKULL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("§6成员列表");
        meta.setLore(guild.getMembersList(true));
        icon.setItemMeta(meta);
        return icon;
    }
}
