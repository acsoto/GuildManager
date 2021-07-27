package com.mcatk.guildmanager.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuildIcon extends ItemStack {
    private String id;
    
    public GuildIcon(Material type, String id) {
        super(type);
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
}
