package com.mcatk.guildmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GuildAdmin implements CommandExecutor {
    String MsgPrefix = "§d§l系统 §7>>> §a";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("guildmanager.admin")){
            sender.sendMessage(MsgPrefix+"§c你没有权限执行该指令");
            return true;
        }
        if(args.length==0||args.length==1){
            sender.sendMessage("§e------------ADMIN帮助------------");
            sender.sendMessage("§a/gmgadmin create <guild> §2创建公会");
            sender.sendMessage("§a/gmgadmin remove <guild> §2移除公会");
            sender.sendMessage("§a/gmgadmin check <guild> §2查看公会情况");
            sender.sendMessage("§a/gmgadmin rename <guild> <player> §2修改公会名");
            sender.sendMessage("§a/gmgadmin sc <guild> <player>  §2设置会长");
            sender.sendMessage("§a/gmgadmin addmem <guild> <player> §2添加成员");
            sender.sendMessage("§a/gmgadmin removemem <guild> <player> §2删除成员");
            return true;
        }
        Guild guild = GuildManager.plugin.getGuild(args[1]);
        if(args[0].equalsIgnoreCase("create")){
            GuildManager.plugin.newGuild(args[1]);
            sender.sendMessage(MsgPrefix+"创建成功");
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
                sender.sendMessage(guild.getStatus());
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
            if (args.length<3){
                sender.sendMessage(MsgPrefix+"§c缺少参数");
                return true;
            }
            GuildManager.plugin.setChairman(args[1],args[2]);
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
        sender.sendMessage(MsgPrefix+"§c指令输入错误");
        return false;
    }
}
