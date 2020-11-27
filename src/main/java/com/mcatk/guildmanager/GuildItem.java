package com.mcatk.guildmanager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuildItem implements Listener {
    final String MsgPrefix = "§6§l公会系统 §7>>> §a";
    final String ErrorPrefix = "§4§l错误 §7>>> §c";
    private final ItemStack tpTicket ;
    private final Guilds guilds = GuildManager.plugin.guilds;

    public GuildItem() {
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


    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        if(event.getAction()== Action.RIGHT_CLICK_AIR){
            Player player = event.getPlayer();
            if(player.getInventory().getItemInMainHand().isSimilar(tpTicket)){
                String playerID = player.getName();
                Guild guild = guilds.getPlayersGuild(playerID);
                if(guild==null)
                    player.sendMessage(MsgPrefix+"你没有公会");
                else if (!guild.isLeader(playerID))
                    player.sendMessage(MsgPrefix+"只有会长/副会长/管理员可以使用");
                else {
                    player.sendMessage(MsgPrefix + "已发动召集令");
                    consumeItem(player.getInventory().getItemInMainHand());
                }
            }
        }
    }

    void consumeItem(ItemStack item){
        int n = item.getAmount();
        n-=1;
        item.setAmount(n);
    }
}

