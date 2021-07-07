package com.mcatk.guildmanager.command;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.GuildGui;
import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.Guilds;
import com.mcatk.guildmanager.Msg;
import com.mcatk.guildmanager.msgs.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildCommand implements CommandExecutor {
    
    private Guilds guilds;
    private CommandSender sender;
    private String[] args;
    private Guild guild;
    
    public GuildCommand() {
        this.guilds = GuildManager.getPlugin().getGuilds();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.args = args;
        this.guild = guilds.getPlayersGuild(sender.getName());
        if (args.length == 0) {
            printHelp();
            return true;
        }
        onCommandWithoutGuild();
        //以下要求发送者在一个公会之中
        if (guild != null) {
            onCommandWithGuild();
        }
        return true;
    }
    
    private void onCommandWithoutGuild() {
        if (args[0].equalsIgnoreCase("gui")) {
            new GuildGui(GuildManager.getPlugin()).openGui((Player) sender);
        }
        if (args[0].equalsIgnoreCase("apply")) {
            apply();
        }
        if (args[0].equalsIgnoreCase("check")) {
            check();
        }
        if (args[0].equalsIgnoreCase("tp")) {
            tp();
        }
        if (args[0].equalsIgnoreCase("create")) {
            create();
        }
    }
    
    private void onCommandWithGuild() {
        if (args[0].equalsIgnoreCase("t")) {
            GuildManager.getPlugin().tpGuild(guild.getName(), sender.getName());
            sender.sendMessage(Msg.INFO + "§a传送成功");
        }
        if (args[0].equalsIgnoreCase("s")) {
            sender.sendMessage(guild.checkStatus());
        }
        if (args[0].equalsIgnoreCase("offer")) {
            offer();
        }
        if (args[0].equalsIgnoreCase("msg")) {
            msg();
        }
        if (args[0].equalsIgnoreCase("msgs")) {
            sender.sendMessage(guild.getMsgFromBoard());
        }
        if (args[0].equalsIgnoreCase("mems")) {
            sender.sendMessage(guild.checkMemSize());
            sender.sendMessage(guild.listMembers());
        }
        if (args[0].equalsIgnoreCase("amems")) {
            sender.sendMessage(guild.checkAdvancedMemSize());
            sender.sendMessage(guild.listAdvancedMembers());
        }
        if (args[0].equalsIgnoreCase("memgui")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Msg.ERROR + "§c该指令只能由玩家发出");
                return;
            }
            new GuildGui(GuildManager.getPlugin()).openMemGui((Player) sender, guild);
            return;
        }
        if (args[0].equalsIgnoreCase("msggui")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Msg.ERROR + "§c该指令只能由玩家发出");
                return;
            }
            new GuildGui(GuildManager.getPlugin()).openMsgGui((Player) sender, guild);
        }
    }
    
    private void apply() {
        if (args.length != 2) {
            sender.sendMessage(Msg.ERROR + "参数错误");
        } else if (guilds.getPlayersGuild(sender.getName()) != null) {
            sender.sendMessage(Msg.ERROR + "已有公会");
        } else {
            Guild guild = guilds.getGuild(args[1]);
            if (guild == null) {
                sender.sendMessage(Msg.ERROR + "不存在公会");
            } else {
                if (guilds.isPlayerInAnyApplicantList(sender.getName())) {
                    sender.sendMessage(Msg.ERROR + "今天已经申请过公会，明天再试");
                } else {
                    guild.getApplicantList().add(sender.getName());
                    sender.sendMessage(Msg.INFO + "申请成功，等待通过");
                }
            }
        }
    }
    
    private void check() {
        if (args.length == 1) {
            sender.sendMessage(Msg.INFO + "§c缺少参数");
        } else if (guilds.hasGuild(args[1])) {
            Guild guild = guilds.getGuild(args[1]);
            if (guild == null) {
                sender.sendMessage(Msg.ERROR + "不存在此公会");
                return;
            }
            sender.sendMessage(guild.checkStatus());
        } else {
            sender.sendMessage(Msg.INFO + "§c不存在此公会");
        }
    }
    
    private void tp() {
        if (args.length == 1) {
            sender.sendMessage(Msg.INFO + "§c缺少参数");
        } else if (guilds.hasGuild(args[1])) {
            String p = sender.getName();
            GuildManager.getPlugin().tpGuild(args[1], p);
            sender.sendMessage(Msg.INFO + "§a传送成功");
        } else {
            sender.sendMessage(Msg.INFO + "§c不存在此公会");
        }
    }
    
    private void create() {
        if (args.length != 2) {
            sender.sendMessage(Msg.ERROR + "参数错误");
            return;
        }
        if (!isAlphabet(args[1])) {
            sender.sendMessage(Msg.ERROR + "ID只能是小写字母");
            return;
        }
        Guild guild = guilds.getPlayersGuild(sender.getName());
        if (guild != null) {
            sender.sendMessage(Msg.ERROR + "你已在公会" + guild.getName());
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Msg.ERROR + "§c该指令只能由玩家发出");
            return;
        }
        if (GuildManager.getPlugin().takePlayerMoney((Player) sender, 500000)) {
            guilds.addGuild(args[1], sender.getName());
            sender.sendMessage(Msg.INFO + "创建成功");
            GuildManager.getPlugin().logInfo("玩家" + sender.getName() + "创建了公会" + args[1]);
        } else {
            sender.sendMessage(Msg.ERROR + "AC点不足！");
        }
    }
    
    private void offer() {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Msg.ERROR + "§c该指令只能由玩家发出");
            return;
        }
        String p = sender.getName();
        if (args.length != 2) {
            sender.sendMessage(Msg.INFO + "§c参数错误");
            return;
        }
        if (!isLegalMoney(args[1])) {
            sender.sendMessage(Msg.INFO + "§c必须是整数！");
            return;
        }
        int n;
        try {
            n = Integer.parseInt(args[1]);
        } catch (ClassCastException e) {
            sender.sendMessage(Msg.INFO + "§c必须是整数！");
            return;
        }
        if (!isLegalMoneyToCash(n)) {
            sender.sendMessage(Msg.INFO + "§c必须是10000的整数倍！");
            return;
        }
        if (GuildManager.getPlugin().takePlayerMoney((Player) sender, n)) {
            guild.addCash(n / 10000);
            //add contribution and check if is full.
            if (!guild.getMember(p).addContribution(n / 10000)) {
                sender.sendMessage(Msg.INFO + "您的贡献值已满，无法继续增长");
            }
            sender.sendMessage(
                    Msg.INFO + "§a成功为" + guild.getName() +
                            "§a捐赠" + n + "AC" + "折合为" + (n / 10000) + "公会资金"
            );
            GuildManager.getPlugin().logInfo(p + "捐献了" + n + "给" + guild.getName());
        } else {
            sender.sendMessage(Msg.ERROR + "AC点不足！");
        }
    }
    
    private void msg() {
        if (args.length == 1) {
            sender.sendMessage("§e§l公会留言板:");
            sender.sendMessage("§e留言板仅用于成员当日通信，每日清空，指令如下");
            sender.sendMessage("§a/gmg msg <内容> §2在留言板写下内容");
            sender.sendMessage("§a/gmg msgs §2查看留言板");
            return;
        }
        if (args.length == 2) {
            if (guild.addMsgToBoard(args[1])) {
                sender.sendMessage(Msg.INFO + "添加成功");
            } else {
                sender.sendMessage(Msg.INFO + "留言板已满，请提醒会长清理");
            }
        } else {
            sender.sendMessage(Msg.ERROR + "不可以有空格哦");
        }
    }
    
    Boolean isLegalMoney(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }
    
    Boolean isLegalMoneyToCash(int money) {
        return (money % 10000) == 0;
    }
    
    Boolean isAlphabet(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!(c >= 'a' && c <= 'z')) {
                return false;
            }
        }
        return true;
    }
    
    private void printHelp() {
        sender.sendMessage("§e------------公会帮助------------");
        sender.sendMessage("§a/gmg gui  §2公会列表");
        sender.sendMessage("§a/gmg apply <ID>  §2申请加入公会");
        sender.sendMessage("§a/gmg check <ID>  §2查看公会详情");
        sender.sendMessage("§a/gmg tp <guild> §2传送到某公会主城");
        sender.sendMessage("§a/gmg create <ID> §2创建公会（ID必须为英文）");
        sender.sendMessage("§a/gmg t §2传送到自己的公会主城");
        sender.sendMessage("§a/gmg s §2查看公会状态");
        sender.sendMessage("§a/gmg offer <AC点> §2捐助公会资金 1wAC = 1GuildCash");
        sender.sendMessage("§a/gmg msg §2公会留言");
        sender.sendMessage("§a/gmg mems §2查看公会成员列表");
        sender.sendMessage("§a/gmg amems §2查看公会名高级成员列表");
        sender.sendMessage("§a/gmg memgui §2查看公会成员菜单");
        sender.sendMessage("§a/gmg msggui §2查看留言板菜单");
    }
}
