package com.mcatk.guildmanager.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {
    //禁止玩家拿走物品
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        boolean unClickableGui =
                event.getInventory().getTitle().equalsIgnoreCase("§6公会列表") ||
                        event.getInventory().getTitle().contains("成员") ||
                        event.getInventory().getTitle().contains("留言板");
        if (unClickableGui) {
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
