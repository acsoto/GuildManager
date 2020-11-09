package com.mcatk.guildmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildCommand implements CommandExecutor {
    String MsgPrefix = "§d§l系统 §7>>> §a";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0){
            sender.sendMessage("§e------------普通帮助------------");
            sender.sendMessage("§a/gmg list  §2公会列表");
            sender.sendMessage("§a/gmg check <ID>  §2查看公会详情");
            sender.sendMessage("§a/gmg tp <guild> §2传送到某公会主城");
            sender.sendMessage("§a/gmg s §2传送到自己的公会主城");
            sender.sendMessage("§a/gmg offer <AC点> §2捐助公会资金 1wAC = 1GuildCash");
            sender.sendMessage("§e------------会长帮助------------");
            sender.sendMessage("§a/gmg members  §2玩家列表");
            sender.sendMessage("§a/gmg setname <name>  §2公会改名");
            sender.sendMessage("§a/gmg add <player>  §2增加玩家");
            sender.sendMessage("§a/gmg remove <player>  §2删除玩家");
            sender.sendMessage("§a/gmg res create §2公会圈地(工具选点后输入该指令)");
            sender.sendMessage("§a/gmg res remove  §2删除公会领地");
            sender.sendMessage("§a/gmg setwarp  §2设置公会领地标");
            sender.sendMessage("§a/gmg delwarp  §2删除公会领地标");
            return true;
        }
        GuildManager plugin = GuildManager.plugin;
        if(args[0].equalsIgnoreCase("list")){
            GuildManager.plugin.listGuilds(sender);
            return true;
        }
        if(args[0].equalsIgnoreCase("check")){
            if(args.length==1)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(GuildManager.plugin.hasGuild(args[1])){
                Guild guild= GuildManager.plugin.getGuild(args[1]);
                sender.sendMessage(guild.getStatus());
            }
            else sender.sendMessage(MsgPrefix+"§c不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("tp")){
            if(args.length==1)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(GuildManager.plugin.hasGuild(args[1])){
                String p= sender.getName();
                plugin.tpGuild(args[1],p);
                sender.sendMessage(MsgPrefix+"§a传送成功");
            }
            else sender.sendMessage(MsgPrefix+"§c不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("s")){
            String p = sender.getName();
            Guild g = GuildManager.plugin.getPlayersGuild(p);
            if(g!=null){
                plugin.tpGuild(g.getName(),p);
                sender.sendMessage(MsgPrefix+"§a传送成功");
            }
            else sender.sendMessage(MsgPrefix+"§c你不在任何公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("offer")){
            if(!(sender instanceof Player)){
                sender.sendMessage(MsgPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            if(args.length!=2) {
                sender.sendMessage(MsgPrefix + "§c参数错误");
                return true;
            }
            Player p = (Player)sender;
            Guild g = GuildManager.plugin.getPlayersGuild(p.getName());
            if(g!=null){
                if(!isLegalMoney(args[1])){
                    sender.sendMessage(MsgPrefix+"§c必须是整数！");
                    return true;
                }
                long n = Integer.parseInt(args[1]);
                if(!isLegalMoneyToCash(n)){
                    sender.sendMessage(MsgPrefix+"§c必须是10000的整数倍！");
                    return true;
                }
                if(plugin.takePlayerMoney(p,n)) {
                g.addCash((int)(n/10000));
                sender.sendMessage(MsgPrefix+"§a成功为"+g.getName()+"§a捐赠"+n+"AC"+"折合为"+(n/10000)+"公会资金");
                }
            }
            else sender.sendMessage(MsgPrefix+"§c你不在任何公会");
            return true;
        }
        //会长操作
        Guild guild=GuildManager.plugin.getChairmansGuild(sender.getName());
        if(guild==null){
            sender.sendMessage(MsgPrefix+"§c你不是会长");
            return true;
        }
        //以下为会长操作
        if(args[0].equalsIgnoreCase("members")){
            sender.sendMessage(guild.listMembers());
            return true;
        }
        if(args[0].equalsIgnoreCase("add")){
            if(args.length<2)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(guild.getMembers().contains(args[1])){
                sender.sendMessage(MsgPrefix + "§c该玩家已存在于本公会");
            }
            else if(guild.addMembers(args[1]))
                sender.sendMessage(MsgPrefix+"增加成功");
            else
                sender.sendMessage(MsgPrefix+"成员已满");
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")){
            if(args.length<2)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(guild.getRemoveMemLimitFlag()>0){
                sender.sendMessage(MsgPrefix + "§c已超过今日删除玩家次数，请明天再试");
            }
            else if(guild.removeMembers(args[1])) {
                guild.addRemoveMemLimitFlag();
                sender.sendMessage(MsgPrefix + "删除成功");
            }
            else
                sender.sendMessage(MsgPrefix + "不存在该玩家");
            return true;
        }
        if (args[0].equalsIgnoreCase("setname")){
            if(args.length<2){
                sender.sendMessage(MsgPrefix+"§c缺少参数");
                return true;
            }
            guild.setName(args[1]);
            sender.sendMessage(MsgPrefix + "成功修改为"+args[1]);
            return true;
        }
        if (args[0].equalsIgnoreCase("res")){
            if(args.length==1)
                sender.sendMessage(MsgPrefix+"§c缺少参数create或remove");
            else if(args[1].equalsIgnoreCase("create")){
                if(guild.getResidenceFLag()){
                    sender.sendMessage(MsgPrefix + "请勿重复设置领地,领地 guild_"+guild.ID+" 已存在");
                }
                else {
                    guild.createResidence((Player) sender);
                }
            }
            else if(args[1].equalsIgnoreCase("remove")){
                if(guild.getResidenceFLag()){
                    guild.removeResidence();
                    sender.sendMessage(MsgPrefix + "领地 guild_"+guild.ID+" 删除成功");
                }
                else {
                    sender.sendMessage(MsgPrefix + "尚未设置领地");
                    sender.sendMessage(MsgPrefix + "请使用工具选点后输入创建指令");
                }
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("setwarp")){
            plugin.setWarp((Player)sender,guild.ID);
        }
        if(args[0].equalsIgnoreCase("delwarp")){
            plugin.delWarp(guild.ID);
        }
        sender.sendMessage(MsgPrefix+"§c指令输入错误");
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

    Boolean isLegalMoneyToCash(long money){
        return (money % 10000) == 0;
    }
}
