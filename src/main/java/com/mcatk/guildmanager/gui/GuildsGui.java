package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.GuildBasicInfo;
import com.mcatk.guildmanager.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuildsGui {

    public Inventory getGuildsGui() {
        Inventory gui = Bukkit.createInventory(GuiType.GUILDS_GUI, 54, "§6公会列表");
        for (Guild guild : SQLManager.getInstance().getAllGuilds()) {
            ItemStack button = getAnGuildButton(guild);
            gui.addItem(button);
        }
        gui.setItem(53, getQuitIcon());
        return gui;
    }

    private ItemStack getAnGuildButton(Guild guild) {
        GuildIcon item = new GuildIcon(Material.STAINED_GLASS, guild.getId());
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        meta.setDisplayName(guild.getGuildName());
        ArrayList<String> des = new ArrayList<>();
        des.add("§0-" + guild.getId());
        des.add("§2公会ID: §a" + guild.getId());
        des.add("§2会长: §a" + guild.getChairman());
        des.add("§2成员: §a" + SQLManager.getInstance().getGuildMembers(guild.getId()).size() + "§7/§2" + GuildBasicInfo.getMaxPlayer(guild.getLevel()));
        des.add("§2最大高级成员数: §a" + GuildBasicInfo.getMaxAdvancedPlayer(guild.getLevel()));
        des.add("§2等级: §a" + guild.getLevel());
        des.add("§2积分: §a" + guild.getPoints());
        des.add("§2资金: §a" + guild.getCash());
        meta.setLore(des);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getQuitIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("返回");
        icon.setItemMeta(meta);
        return icon;
    }

}
