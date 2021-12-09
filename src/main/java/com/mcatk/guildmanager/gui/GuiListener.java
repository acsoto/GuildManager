package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.Msg;
import com.mcatk.guildmanager.Operation;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.sql.SQLManager;
import org.bukkit.Material;
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
        if (event.getInventory().getHolder() == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder().equals(GuiType.GUILDS_GUI)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta() != null) {
                    if (event.getCurrentItem().getType().equals(Material.AIR)) {
                    } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals("返回")) {
                        ((Player) event.getWhoClicked()).chat("/menu");
                    } else {
                        String guildID =
                                event.getCurrentItem().getItemMeta().getLore().get(0).split(":")[1];
                        Guild guild =
                                SQLManager.getInstance().getGuild(guildID);
                        player.openInventory(new GuildGui(guild).getGui());
                    }
                }
            }
        } else if (event.getInventory().getHolder().equals(GuiType.GUILD_GUI)) {
            event.setCancelled(true);
            Guild guild =
                    SQLManager.getInstance().getGuild(event.getInventory().getTitle().split(":")[1]);
            if (event.getCurrentItem().getType().equals(Material.AIR)) {
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Msg.GUILD_GUI_TP.toString())) {
                new Operation().tpGuild(guild, event.getWhoClicked().getName());
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Msg.GUI_BACK.toString())) {
                player.openInventory(new GuildsGui().getGuildsGui());
            }
        }
    }

}
