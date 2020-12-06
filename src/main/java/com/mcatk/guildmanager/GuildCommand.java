package com.mcatk.guildmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class GuildCommand implements CommandExecutor {
    final String MsgPrefix = "§6§l公会系统 §7>>> §a";
    final String ErrorPrefix = "§4§l错误 §7>>> §c";
    GuildManager plugin;
    Guilds guilds;
    GuildCommand(GuildManager plugin, Guilds guilds){
        this.plugin = plugin;
        this.guilds = guilds;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0){
            sender.sendMessage("§e------------公会帮助------------");
            sender.sendMessage("§a/gmg list  §2公会列表");
            sender.sendMessage("§a/gmg gui  §2公会列表");
            sender.sendMessage("§a/gmg check <ID>  §2查看公会详情");
            sender.sendMessage("§a/gmg tp <guild> §2传送到某公会主城");
            sender.sendMessage("§a/gmg create <ID> §2创建公会（ID必须为英文）");
            sender.sendMessage("§a/gmg t §2传送到自己的公会主城");
            sender.sendMessage("§a/gmg s §2查看公会状态");
            sender.sendMessage("§a/gmg r §2打开公会仓库");
            sender.sendMessage("§a/gmg offer <AC点> §2捐助公会资金 1wAC = 1GuildCash");
            sender.sendMessage("§a/gmg msg §2公会留言");
            sender.sendMessage("§a/gmg mems §2查看公会成员列表");
            sender.sendMessage("§a/gmg amems §2查看公会名高级成员列表");
            sender.sendMessage("§a/gmg memgui §2查看公会成员菜单");
            sender.sendMessage("§a/gmg msggui §2查看留言板菜单");
            return true;
        }
        if(args[0].equalsIgnoreCase("list")){
            guilds.listGuilds(sender);
            return true;
        }
        if(args[0].equalsIgnoreCase("gui")){
            plugin.gui.openGUI((Player)sender);
            return true;
        }
        if(args[0].equalsIgnoreCase("check")){
            if(args.length==1)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(guilds.hasGuild(args[1])){
                Guild guild= guilds.getGuild(args[1]);
                if(guild==null){
                    sender.sendMessage(ErrorPrefix+"不存在此公会");
                    return true;
                }
                sender.sendMessage(guild.checkStatus());
            }
            else sender.sendMessage(MsgPrefix+"§c不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("tp")){
            if(args.length==1)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(guilds.hasGuild(args[1])){
                String p= sender.getName();
                plugin.tpGuild(args[1],p);
                sender.sendMessage(MsgPrefix+"§a传送成功");
            }
            else sender.sendMessage(MsgPrefix+"§c不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("create")){
            if(args.length!=2){
                sender.sendMessage(ErrorPrefix+"参数错误");
                return true;
            }
            if(!isAlphabet(args[1])){
                sender.sendMessage(ErrorPrefix+"ID只能是小写字母");
                return false;
            }
            Guild guild = guilds.getPlayersGuild(sender.getName());
            if(guild!=null){
                sender.sendMessage(ErrorPrefix+"你已在公会"+guild.getName());
                return true;
            }
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            if(plugin.takePlayerMoney((Player)sender,plugin.getReqCreateGuildMoney())){
                guilds.newGuild(args[1],sender.getName());
                sender.sendMessage(MsgPrefix+"创建成功");
                plugin.logInfo("玩家"+sender.getName()+"创建了公会"+args[1]);
            }
            else sender.sendMessage(ErrorPrefix+"AC点不足！");
            return true;
        }
        //以下要求发送者在一个公会之中
        Guild guild = guilds.getPlayersGuild(sender.getName());
        if(guild==null){
            sender.sendMessage(MsgPrefix+"§c你不在任何公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("t")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            plugin.tpGuild(guild.getName(),sender.getName());
            sender.sendMessage(MsgPrefix+"§a传送成功");
            return true;
        }

        if(args[0].equalsIgnoreCase("s")){
            sender.sendMessage(guild.checkStatus());
            return true;
        }
        if(args[0].equalsIgnoreCase("r")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            try {
                plugin.guildRepository.openRepos((Player)sender,guild);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("offer")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            String p =sender.getName();
            if(args.length!=2) {
                sender.sendMessage(MsgPrefix + "§c参数错误");
                return true;
            }
            if(!isLegalMoney(args[1])){
                sender.sendMessage(MsgPrefix+"§c必须是整数！");
                return true;
            }
            int n;
            try{
                n = Integer.parseInt(args[1]);
            }catch (ClassCastException e){
                sender.sendMessage(MsgPrefix+"§c必须是整数！");
                return true;
            }
            if(!isLegalMoneyToCash(n)){
                sender.sendMessage(MsgPrefix+"§c必须是10000的整数倍！");
                return true;
            }
            if(plugin.takePlayerMoney((Player) sender,n)) {
            guild.addCash(n/10000);
            //add contribution and check if is full.
                if(!guild.getMember(p).addContribution(n/10000))
                    sender.sendMessage(MsgPrefix+"您的贡献值已满，无法继续增长");
                sender.sendMessage(MsgPrefix+"§a成功为"+guild.getName()+"§a捐赠"+n+"AC"+"折合为"+(n/10000)+"公会资金");
                plugin.logInfo(p+"捐献了"+ n +"给"+guild.getName());
            }
            else sender.sendMessage(ErrorPrefix+"AC点不足！");
            return true;
        }
        if(args[0].equalsIgnoreCase("msg")){
            if(args.length==1){
                sender.sendMessage("§e§l公会留言板:");
                sender.sendMessage("§e留言板仅用于成员当日通信，每日清空，指令如下");
                sender.sendMessage("§a/gmg msg <内容> §2在留言板写下内容");
                sender.sendMessage("§a/gmg msgs §2查看留言板");
                return true;
            }
            if(args.length==2){
                if(guild.addMsgToBoard(args[1])){
                    sender.sendMessage(MsgPrefix+"添加成功");
                }
                else sender.sendMessage(MsgPrefix+"留言板已满，请提醒会长清理");
            }
            else sender.sendMessage(ErrorPrefix+"不可以有空格哦");
            return true;
        }
        if(args[0].equalsIgnoreCase("msgs")){
            sender.sendMessage(guild.getMsgFromBoard());
            return true;
        }
        if(args[0].equalsIgnoreCase("name")){
            sender.sendMessage(guild.checkGuildName());
            return true;
        }
        if(args[0].equalsIgnoreCase("chairman")){
            sender.sendMessage(guild.checkChairman());
            return true;
        }
        if(args[0].equalsIgnoreCase("cash")){
            sender.sendMessage(guild.checkCash());
            return true;
        }
        if(args[0].equalsIgnoreCase("points")){
            sender.sendMessage(guild.checkPoints());
            return true;
        }
        if(args[0].equalsIgnoreCase("mems")){
            sender.sendMessage(guild.checkMemSize());
            sender.sendMessage(guild.listMembers());
            return true;
        }
        if(args[0].equalsIgnoreCase("amems")){
            sender.sendMessage(guild.checkAdvancedMemSize());
            sender.sendMessage(guild.listAdvancedMembers());
            return true;
        }
        if(args[0].equalsIgnoreCase("memgui")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            plugin.gui.openMemGUI((Player) sender,guild);
            return true;
        }
        if(args[0].equalsIgnoreCase("msggui")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            plugin.gui.openMsgGUI((Player)sender,guild);
            return true;
        }
        /////////////////////////////////
        sender.sendMessage(ErrorPrefix+"§c指令输入错误");
        return false;
    }


    Boolean isLegalMoney(String s){
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(!(c>='0'&&c<='9')){
                return false;
            }
        }
        return true;
    }

    Boolean isLegalMoneyToCash(int money){
        return (money % 10000) == 0;
    }

    Boolean isAlphabet(String str){
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(!(c>='a'&&c<='z'))
                return false;
        }
        return true;
    }
}
