package com.mcatk.guildmanager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuildItem {

    public ItemStack getTpTicket() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6召集令");
        ArrayList<String> description = new ArrayList<>();
        description.add("§e右键使用");
        description.add("§a向在线的公会全体人员发出传送邀请");
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }
}
