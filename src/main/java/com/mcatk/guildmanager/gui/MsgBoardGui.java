package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.Guild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MsgBoardGui {
    
    public Inventory getMsgBoardGui(Guild guild) {
        ArrayList<String> msgBoard = guild.getMsgBoard();
        Inventory gui = Bukkit.createInventory(null, 18, guild.getName() + "§6留言板");
        for (String str :
                msgBoard) {
            ItemStack button = getAnMsgButton(str);
            gui.addItem(button);
        }
        return gui;
    }
    
    private ItemStack getAnMsgButton(String str) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(str);
        item.setItemMeta(meta);
        return item;
    }
    
}
