package com.mcatk.guildmanager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuildItem {

    public static ItemStack getTpTicket() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6召集令");
        ArrayList<String> description = new ArrayList<>();
        description.add("§e输入/gmg tpall使用");
        description.add("§a向在线的公会全体人员发出传送邀请");
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }

    public static Boolean oneOfThemIsTpTicket(ItemStack itemInHand){
        ItemStack item = itemInHand.clone();
        item.setAmount(1);
        return item.equals(getTpTicket());
    }
}
