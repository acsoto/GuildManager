package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.GuildManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class GuildsGui {
    
    public Inventory getGuildsGui() {
        Inventory gui = Bukkit.createInventory(GuiHolder.getGuiHolder(), 54, "§6公会列表");
        for (String key :
                GuildManager.getPlugin().getGuilds().getGuildMap().keySet()) {
            Guild guild = GuildManager.getPlugin().getGuilds().getGuild(key);
            ItemStack button = getAnGuildButton(guild);
            gui.addItem(button);
        }
        gui.setItem(53, getQuitIcon());
        return gui;
    }
    
    private ItemStack getAnGuildButton(Guild guild) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
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
    
    private ItemStack getQuitIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("返回");
        icon.setItemMeta(meta);
        return icon;
    }
    
}
