package com.mcatk.guildmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinListener implements Listener {
    GuildManager plugin = GuildManager.plugin;
    String MsgPrefix = "§6§l公会系统 §7>>> §a";
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String  playerID = player.getName();
        Guild guild = plugin.getChairmansGuild(playerID);
        //防止服务器意外崩溃导致玩家OP权限未撤销
        if(!(playerID.equals("zhou_zhou")||playerID.equals("Iceborne"))){
            if(player.isOp()){
                plugin.sendConsoleCmd("kick "+ playerID);
                player.setOp(false);
            }
        }
        if(guild!=null){
            plugin.getServer().broadcastMessage(MsgPrefix+guild.getName()+"§2会长§e"+player.getName()+"§2已上线");
        }
        //check if player has guild,if not return
        guild = plugin.getPlayersGuild(player.getName());
        if(guild==null)
            return;

        //tell player who is online in the guild
        StringBuilder msg = new StringBuilder(MsgPrefix + "§2您的公会" + guild.getName() + "§2现在在线玩家:");
        for (Player p : plugin.getServer().getOnlinePlayers()){
            if(guild.hasPlayer(p.getName())){
                msg.append("§e").append(p.getName()).append(",");
            }
        }
        player.sendMessage(msg.toString());
        //tell player he is advanced
        if(guild.isAdvancedPlayer(player.getName())){
            player.sendMessage(MsgPrefix+"§2您是公会高级成员，可以前往公会广场");
        }
        //tell player situation about msgBoard if it is not empty
        if(!guild.isMsgBoardEmpty()){
            player.sendMessage(guild.getMsgFromBoard());
        }
    }
}
