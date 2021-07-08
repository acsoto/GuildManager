package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.Member;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class MemGui {
    
    public Inventory getMemGui(Guild guild) {
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
    
    private ItemStack getAnMemberButton(Guild guild, Member player) {
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
}
