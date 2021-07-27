package com.mcatk.guildmanager.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public enum GuiType implements InventoryHolder {
    GUILDS_GUI,
    GUILD_GUI,
    ;
    
    @Override
    public Inventory getInventory() {
        return null;
    }
}
