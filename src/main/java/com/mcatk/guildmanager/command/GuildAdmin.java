package com.mcatk.guildmanager.command;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.Guilds;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GuildAdmin implements CommandExecutor {
    private final String msgPrefix = "§6§l公会系统 §7>>> §a";
    private final String errorPrefix = "§4§l错误 §7>>> §c";
    private final GuildManager plugin;
    private final Guilds guilds;
    
    public GuildAdmin(GuildManager plugin) {
        this.plugin = plugin;
        this.guilds = plugin.getGuilds();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("guildmanager.admin")) {
            sender.sendMessage(msgPrefix + "§c你没有权限执行该指令");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§e------------ADMIN帮助------------");
            sender.sendMessage("§a/gmgadmin reload §2重载插件");
            sender.sendMessage("§a/gmgadmin create <guild> <player>§2创建公会");
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
        if (args[0].equalsIgnoreCase("reload")) {
            
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(msgPrefix + "缺少参数");
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 3) {
                guilds.addGuild(args[1], args[2]);
                sender.sendMessage(msgPrefix + "创建成功");
                return true;
            }
            sender.sendMessage(errorPrefix + "缺少参数");
            return true;
        }
        Guild guild = guilds.getGuild(args[1]);
        if (guild == null) {
            sender.sendMessage(errorPrefix + "不存在此公会");
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (guilds.removeGuild(args[1])) {
                sender.sendMessage(msgPrefix + "删除成功");
            } else {
                sender.sendMessage(msgPrefix + "§c不存在此公会");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("check")) {
            if (guilds.hasGuild(args[1])) {
                sender.sendMessage(guild.checkStatus());
            } else {
                sender.sendMessage(msgPrefix + "§c不存在此公会");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("rename")) {
            if (args.length < 3) {
                sender.sendMessage(msgPrefix + "§c缺少参数");
                return true;
            } else if (guilds.hasGuild(args[1])) {
                guild.setName(args[2]);
                sender.sendMessage(msgPrefix + "已修改为" + args[2]);
            } else {
                sender.sendMessage(msgPrefix + "§c不存在此公会");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("addmem")) {
            if (guild.addMembers(args[2])) {
                sender.sendMessage(msgPrefix + "添加成功");
            } else {
                sender.sendMessage(msgPrefix + "成员已满");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("removemem")) {
            if (guild.removeMembers(args[2])) {
                sender.sendMessage(msgPrefix + "删除成功");
            } else {
                sender.sendMessage(msgPrefix + "不存在该玩家");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("addcash")) {
            if (args.length < 3) {
                sender.sendMessage(msgPrefix + "§c缺少参数");
                return true;
            }
            int n = Integer.parseInt(args[2]);
            guild.addCash(n);
            sender.sendMessage(msgPrefix + "操作成功");
            return true;
        }
        if (args[0].equalsIgnoreCase("takecash")) {
            if (args.length < 3) {
                sender.sendMessage(msgPrefix + "§c缺少参数");
                return true;
            }
            int n = Integer.parseInt(args[2]);
            if (guild.takeCash(n)) {
                sender.sendMessage(msgPrefix + "操作成功");
            } else {
                sender.sendMessage(msgPrefix + "§c错误：超过其资金");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("addpoints")) {
            if (args.length < 3) {
                sender.sendMessage(msgPrefix + "§c缺少参数");
                return true;
            }
            int n = Integer.parseInt(args[2]);
            guild.addPoints(n);
            sender.sendMessage(msgPrefix + "操作成功");
            return true;
        }
        if (args[0].equalsIgnoreCase("takepoints")) {
            if (args.length < 3) {
                sender.sendMessage(msgPrefix + "§c缺少参数");
                return true;
            }
            int n = Integer.parseInt(args[2]);
            if (guild.takePoints(n)) {
                sender.sendMessage(msgPrefix + "操作成功");
            } else {
                sender.sendMessage(msgPrefix + "§c错误：超过其积分");
            }
            return true;
        }
        sender.sendMessage(msgPrefix + "§c指令输入错误");
        return false;
    }
}
