package com.mcatk.guildmanager.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {
    
    private String[] unClickableGuiTitles = {"公会列表", "成员", "留言板"};
    
    //禁止玩家拿走物品
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder().equals(GuiHolder.getGuiHolder())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals("返回")) {
                    ((Player) event.getWhoClicked()).chat("/menu");
                }
            }
            //            player.updateInventory();
            //            if(event.getRawSlot()==0){
            //                player.closeInventory();
            //                player.openInventory(createGuildsGUI());
            //            }
        }
    }
    
}
