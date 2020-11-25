package com.mcatk.guildmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GuildAdmin implements CommandExecutor {
    final String MsgPrefix = "§6§l公会系统 §7>>> §a";
    final String ErrorPrefix = "§4§l错误 §7>>> §c";
    GuildManager plugin = GuildManager.plugin;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("guildmanager.admin")){
            sender.sendMessage(MsgPrefix+"§c你没有权限执行该指令");
            return true;
        }
        if(args.length==0){
            sender.sendMessage("§e------------ADMIN帮助------------");
            sender.sendMessage("§a/gmgadmin reload §2重载插件");
            sender.sendMessage("§a/gmgadmin create <guild> (player)§2创建公会");
            sender.sendMessage("§a/gmgadmin remove <guild> §2移除公会");
            sender.sendMessage("§a/gmgadmin check <guild> §2查看公会情况");
            sender.sendMessage("§a/gmgadmin rename <guild> <player> §2修改公会名");
            sender.sendMessage("§a/gmgadmin sc <guild> <player>  §2设置会长");
            sender.sendMessage("§a/gmgadmin addmem <guild> <player> §2添加成员");
            sender.sendMessage("§a/gmgadmin removemem <guild> <player> §2删除成员");
            sender.sendMessage("§a/gmgadmin addcash <guild> <cash> §2增加资金");
            sender.sendMessage("§a/gmgadmin takecash <guild> <cash> §2减少资金");
            sender.sendMessage("§a/gmgadmin addpoints <guild> <points> §2增加积分");
            sender.sendMessage("§a/gmgadmin takepoints <guild> <points> §2减少积分");
            return true;
        }
        if(args[0].equalsIgnoreCase("reload")){
            plugin.reloadPlugin();
            return true;
        }
        if(args.length==1){
            sender.sendMessage(MsgPrefix+"缺少参数");
            return true;
        }
        if(args[0].equalsIgnoreCase("create")){
            if(args.length==2){
                plugin.newGuild(args[1]);
                sender.sendMessage(MsgPrefix+"创建成功");
            }
            else if(args.length==3){
                plugin.newGuild(args[1],args[2]);
                sender.sendMessage(MsgPrefix+"创建成功");
            }
            return true;
        }
        Guild guild = plugin.getGuild(args[1]);
        if(guild==null){
            sender.sendMessage(ErrorPrefix+"不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("remove")){
            if(GuildManager.plugin.removeGuild(args[1]))
                sender.sendMessage(MsgPrefix+"删除成功");
            else sender.sendMessage(MsgPrefix+"§c不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("check")){
            if(GuildManager.plugin.hasGuild(args[1])){
                sender.sendMessage(guild.checkStatus());
            }
            else sender.sendMessage(MsgPrefix+"§c不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("rename")){
            if (args.length<3){
                sender.sendMessage(MsgPrefix+"§c缺少参数");
                return true;
            }
            else if(GuildManager.plugin.hasGuild(args[1])){
                guild.setName(args[2]);
                sender.sendMessage(MsgPrefix+"已修改为"+args[2]);
            }
            else sender.sendMessage(MsgPrefix+"§c不存在此公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("sc")){
            if (args.length!=3){
                sender.sendMessage(MsgPrefix+"§c参数有误");
                return true;
            }
            guild.setChairman(args[2]);
            sender.sendMessage(MsgPrefix+"设置成功");
            return true;
        }
        if(args[0].equalsIgnoreCase("addmem")){
            if(guild.addMembers(args[2]))
              sender.sendMessage(MsgPrefix+"添加成功");
            else sender.sendMessage(MsgPrefix+"成员已满");
            return true;
        }
        if(args[0].equalsIgnoreCase("removemem")){
            if(guild.removeMembers(args[2]))
                sender.sendMessage(MsgPrefix+"删除成功");
            else sender.sendMessage(MsgPrefix+"不存在该玩家");
            return true;
        }
        if(args[0].equalsIgnoreCase("addcash")){
            if (args.length<3){
                sender.sendMessage(MsgPrefix+"§c缺少参数");
                return true;
            }
            int n=Integer.parseInt(args[2]);
            guild.addCash(n);
            sender.sendMessage(MsgPrefix+"操作成功");
            return true;
        }
        if(args[0].equalsIgnoreCase("takecash")){
            if (args.length<3){
                sender.sendMessage(MsgPrefix+"§c缺少参数");
                return true;
            }
            int n=Integer.parseInt(args[2]);
            if(guild.takeCash(n))
                sender.sendMessage(MsgPrefix+"操作成功");
            else sender.sendMessage(MsgPrefix+"§c错误：超过其资金");
            return true;
        }
        if(args[0].equalsIgnoreCase("addpoints")){
            if (args.length<3){
                sender.sendMessage(MsgPrefix+"§c缺少参数");
                return true;
            }
            int n=Integer.parseInt(args[2]);
            guild.addPoints(n);
            sender.sendMessage(MsgPrefix+"操作成功");
            return true;
        }
        if(args[0].equalsIgnoreCase("takepoints")){
            if (args.length<3){
                sender.sendMessage(MsgPrefix+"§c缺少参数");
                return true;
            }
            int n=Integer.parseInt(args[2]);
            if(guild.takePoints(n))
                sender.sendMessage(MsgPrefix+"操作成功");
            else sender.sendMessage(MsgPrefix+"§c错误：超过其积分");
            return true;
        }
        sender.sendMessage(MsgPrefix+"§c指令输入错误");
        return false;
    }
}
