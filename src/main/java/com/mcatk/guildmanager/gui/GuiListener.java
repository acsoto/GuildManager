package com.mcatk.guildmanager.gui;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.Msg;
import com.mcatk.guildmanager.Operation;
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
                        String guildId =
                                event.getCurrentItem().getItemMeta().getLore().get(0).split(":")[1];
                        Guild guild =
                                GuildManager.getPlugin().getGuilds().getGuild(guildId);
                        player.openInventory(new GuildGui(guild).getGui());
                    }
                }
            }
        } else if (event.getInventory().getHolder().equals(GuiType.GUILD_GUI)) {
            event.setCancelled(true);
            Guild guild =
                    GuildManager.getPlugin().getGuilds().getGuild(event.getInventory().getTitle().split(":")[1]);
            if (event.getCurrentItem().getType().equals(Material.AIR)) {
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Msg.GUILD_GUI_TP.toString())) {
                new Operation().tpGuild(guild, event.getWhoClicked().getName());
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Msg.GUI_BACK.toString())) {
                player.openInventory(new GuildsGui().getGuildsGui());
            }
        }
    }
    
}
