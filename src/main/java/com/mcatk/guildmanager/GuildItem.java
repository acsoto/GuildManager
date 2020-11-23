package com.mcatk.guildmanager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuildItem {
    private final ItemStack tpTicket ;

    public GuildItem(){
        //构造tpTicket
        tpTicket = new ItemStack(Material.PAPER);
        ItemMeta meta = tpTicket.getItemMeta();
        meta.setDisplayName("§6公会召集令");
        ArrayList<String> description = new ArrayList<>();
        description.add("§e输入/gmg tpall使用");
        description.add("§a向在线的公会全体人员");
        description.add("§a发出传送邀请");
        meta.setLore(description);
        tpTicket.setItemMeta(meta);
    }

    public ItemStack getTpTicket(int amount) {
        ItemStack item = tpTicket.clone();
        item.setAmount(amount);
        return item;
    }

    public Boolean oneOfThemIsTpTicket(ItemStack itemInHand){
        ItemStack item = itemInHand.clone();
        item.setAmount(1);
        return item.equals(getTpTicket(1));
    }

    int buyTpTickets(Guild guild, Player player, int n){
        if(n>10){
            return 0;
        }
        else if (guild.getCash()<n){
            return 1;
        }
        else if (player.getInventory().firstEmpty()==-1){
            return 2;
        }
        guild.takeCash(n);
        player.getInventory().addItem(getTpTicket(n*5));
        return 3;
    }

}

