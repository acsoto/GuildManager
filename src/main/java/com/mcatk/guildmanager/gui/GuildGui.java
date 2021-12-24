package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.Msg;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.sql.SQLManager;
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
        Inventory gui = Bukkit.createInventory(GuiType.GUILD_GUI, 9, guild.getGuildName()+"-"+guild.getId());
        gui.setItem(0, getTpIcon());
        gui.setItem(1, getMemberIcon());
        gui.setItem(8, getQuitIcon());
        return gui;
    }

    private ItemStack getTpIcon() {
        ItemStack icon = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(Msg.GUILD_GUI_TP.toString());
        icon.setItemMeta(meta);
        return icon;
    }

    private ItemStack getMemberIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_HELMET);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("§6成员列表");
        meta.setLore(SQLManager.getInstance().getGuildMembers(guild.getId()));
        icon.setItemMeta(meta);
        return icon;
    }

    private ItemStack getQuitIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(Msg.GUI_BACK.toString());
        icon.setItemMeta(meta);
        return icon;
    }
}
