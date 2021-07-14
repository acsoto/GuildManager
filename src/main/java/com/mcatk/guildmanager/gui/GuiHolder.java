package com.mcatk.guildmanager.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiHolder implements InventoryHolder {
    
    private static GuiHolder holder = null;
    
    @Override
    public Inventory getInventory() {
        return null;
    }
    
    public static GuiHolder getGuiHolder() {
        return holder == null ? holder = new GuiHolder() : holder;
    }
}
