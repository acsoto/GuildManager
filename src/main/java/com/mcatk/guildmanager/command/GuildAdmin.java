package com.mcatk.guildmanager.command;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.Guilds;
import com.mcatk.guildmanager.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GuildAdmin implements CommandExecutor {
    private final Guilds guilds;
    private CommandSender sender;
    private String[] args;
    
    public GuildAdmin() {
        this.guilds = GuildManager.getPlugin().getGuilds();
    }
    
    // usage: /gmga reload|create|remove|check|rename|sc|addmem|removemem|addcash|takecash|addpoints|takepoints
    private void printHelp() {
        sender.sendMessage("§e------------ADMIN帮助------------");
        sender.sendMessage("§a/gmga reload §2重载插件");
        sender.sendMessage("§a/gmga create <guild> <player>§2创建公会");
        sender.sendMessage("§a/gmga remove <guild> §2移除公会");
        sender.sendMessage("§a/gmga check <guild> §2查看公会情况");
        sender.sendMessage("§a/gmga rename <guild> <player> §2修改公会名");
        sender.sendMessage("§a/gmga sc <guild> <player>  §2设置会长");
        sender.sendMessage("§a/gmga addmem <guild> <player> §2添加成员");
        sender.sendMessage("§a/gmga removemem <guild> <player> §2删除成员");
        sender.sendMessage("§a/gmga addcash <guild> <cash> §2增加资金");
        sender.sendMessage("§a/gmga takecash <guild> <cash> §2减少资金");
        sender.sendMessage("§a/gmga addpoints <guild> <points> §2增加积分");
        sender.sendMessage("§a/gmga takepoints <guild> <points> §2减少积分");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.args = args;
        if (!sender.isOp()) {
            return false;
        }
        if (args.length == 0) {
            printHelp();
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(Msg.INFO + "缺少参数");
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 3) {
                guilds.addGuild(args[1], args[2]);
                sender.sendMessage(Msg.INFO + "创建成功");
                return true;
            }
            sender.sendMessage(Msg.ERROR + "缺少参数");
            return true;
        }
        Guild guild = guilds.getGuild(args[1]);
        if (guild == null) {
            sender.sendMessage(Msg.ERROR + "不存在此公会");
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            remove();
        }
        if (args[0].equalsIgnoreCase("rename")) {
            rename(guild);
        }
        if (args[0].equalsIgnoreCase("addmem")) {
            addMember(guild);
        }
        if (args[0].equalsIgnoreCase("removemem")) {
            removeMember(guild);
        }
        if (args[0].equalsIgnoreCase("addcash")) {
            addCash(guild);
        }
        if (args[0].equalsIgnoreCase("takecash")) {
            takeCash(guild);
        }
        if (args[0].equalsIgnoreCase("addpoints")) {
            addPoints(guild);
        }
        if (args[0].equalsIgnoreCase("takepoints")) {
            takePoints(guild);
        }
        //sender.sendMessage(Msg.INFO + "§c指令输入错误");
        return true;
    }
    
    private void remove() {
        if (guilds.removeGuild(args[1])) {
            sender.sendMessage(Msg.INFO + "删除成功");
        } else {
            sender.sendMessage(Msg.INFO + "§c不存在此公会");
        }
    }
    
    private void rename(Guild guild) {
        if (args.length < 3) {
            sender.sendMessage(Msg.INFO + "§c缺少参数");
        } else if (guilds.hasGuild(args[1])) {
            guild.setName(args[2]);
            sender.sendMessage(Msg.INFO + "已修改为" + args[2]);
        } else {
            sender.sendMessage(Msg.INFO + "§c不存在此公会");
        }
    }
    
    private void addMember(Guild guild) {
        if (guild.addMembers(args[2])) {
            sender.sendMessage(Msg.INFO + "添加成功");
        } else {
            sender.sendMessage(Msg.INFO + "成员已满");
        }
    }
    
    private void removeMember(Guild guild) {
        if (guild.removeMembers(args[2])) {
            sender.sendMessage(Msg.INFO + "删除成功");
        } else {
            sender.sendMessage(Msg.INFO + "不存在该玩家");
        }
    }
    
    private void addCash(Guild guild) {
        if (args.length < 3) {
            sender.sendMessage(Msg.INFO + "§c缺少参数");
            return;
        }
        int n = Integer.parseInt(args[2]);
        guild.addCash(n);
        sender.sendMessage(Msg.INFO + "操作成功");
    }
    
    private void takeCash(Guild guild) {
        if (args.length < 3) {
            sender.sendMessage(Msg.INFO + "§c缺少参数");
            return;
        }
        int n = Integer.parseInt(args[2]);
        if (guild.takeCash(n)) {
            sender.sendMessage(Msg.INFO + "操作成功");
        } else {
            sender.sendMessage(Msg.INFO + "§c错误：超过其资金");
        }
    }
    
    private void addPoints(Guild guild) {
        if (args.length < 3) {
            sender.sendMessage(Msg.INFO + "§c缺少参数");
            return;
        }
        int n = Integer.parseInt(args[2]);
        guild.addPoints(n);
        sender.sendMessage(Msg.INFO + "操作成功");
    }
    
    private void takePoints(Guild guild) {
        if (args.length < 3) {
            sender.sendMessage(Msg.INFO + "§c缺少参数");
            return;
        }
        int n = Integer.parseInt(args[2]);
        if (guild.takePoints(n)) {
            sender.sendMessage(Msg.INFO + "操作成功");
        } else {
            sender.sendMessage(Msg.INFO + "§c错误：超过其积分");
        }
    }
    
    // gmgadmin resetname guild
    private void resetNameFlag(Guild guild) {
        guild.setHasChangedName(false);
    }
    
    private boolean sendParaLengthError(int length) {
        if (args.length != length) {
            sender.sendMessage(Msg.ERROR + "§c参数长度有误，请重试。长度应为 " + length);
            return true;
        }
        return false;
    }
}
