package com.mcatk.guildmanager;

import com.mcatk.guildmanager.msgs.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildCommandS implements CommandExecutor {
    private GuildManager plugin;
    private Guilds guilds;
    private GuildItem guildItem;
    private CommandSender sender;
    private String[] args;
    private Guild guild;
    
    GuildCommandS(GuildManager plugin) {
        this.plugin = plugin;
        this.guilds = plugin.getGuilds();
        this.guildItem = new GuildItem(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.args = args;
        this.guild = guilds.getPlayersGuild(sender.getName());
        if (guild == null) {
            sender.sendMessage(Message.ERROR + "您不在任何公会");
            return true;
        }
        if (!guild.hasLeader(sender.getName())) {
            sender.sendMessage(Message.ERROR + "您没有操作该公会的权限");
            return true;
        }
        if (args.length == 0) {
            printHelp();
            return true;
        }
        if (guild.hasLeader(sender.getName())) {
            onCommandLeader();
        }
        if (guild.hasChairman(sender.getName())) {
            onCommandChairman();
        }
        return true;
    }
    
    private void onCommandLeader() {
        if (args[0].equalsIgnoreCase("app")) {
            app();
        }
        if (args[0].equalsIgnoreCase("remove")) {
            remove();
        }
        if (args[0].equalsIgnoreCase("buytpall")) {
            buyTpAll();
        }
        if (args[0].equalsIgnoreCase("clearmsg")) {
            clearMsg();
        }
    }
    
    private void onCommandChairman() {
        if (args[0].equalsIgnoreCase("advance")) {
            advance();
        }
        if (args[0].equalsIgnoreCase("setname")) {
            setName();
        }
        if (args[0].equalsIgnoreCase("levelup")) {
            levelUp();
        }
        if (args[0].equalsIgnoreCase("res")) {
            res();
        }
        if (args[0].equalsIgnoreCase("warp")) {
            warp();
        }
        if (args[0].equalsIgnoreCase("position")) {
            position();
        }
    }
    
    void printHelp() {
        boolean isChairman = guild.getChairman().equalsIgnoreCase(sender.getName());
        boolean isViceChairman = guild.getViceChairman().contains(sender.getName());
        sender.sendMessage("§e------------公会操作帮助------------");
        if (isChairman) {
            sender.sendMessage("§a/gmgs setname <name>  §2公会名称设置");
            sender.sendMessage("§a/gmgs levelup  §2公会升级");
            sender.sendMessage("§a/gmgs res create §2公会圈地(工具选点后输入该指令)");
            sender.sendMessage("§a/gmgs res remove  §2删除公会领地");
            sender.sendMessage("§a/gmgs setwarp  §2设置公会领地标");
            sender.sendMessage("§a/gmgs delwarp  §2删除公会领地标");
            sender.sendMessage("§a/gmgs position set <player> v/m  §2设置玩家为副会长/管理员");
            sender.sendMessage("§a/gmgs position remove <player> v/m §2撤销玩家的副会长/管理员");
            sender.sendMessage("§a/gmgs setally <guildID> §2设置伙伴公会");
        }
        if (isChairman || isViceChairman) {
            sender.sendMessage("§a/gmgs advance add <player>  §2增加玩家到公会广场名单");
            sender.sendMessage("§a/gmgs advance remove <player>  §2从公会广场名单删除玩家");
        }
        sender.sendMessage("§a/gmgs app  §2查看公会加入申请");
        sender.sendMessage("§a/gmgs remove <player>  §2删除玩家");
        sender.sendMessage("§a/gmgs buytpall (num) §2购买公会召集令");
        sender.sendMessage("§a/gmgs tpall  §2发起召集");
        sender.sendMessage("§a/gmgs clearmsg  §2清空留言板");
    }
    
    void app() {
        if (args.length == 1) {
            sender.sendMessage("申请列表:");
            for (String p : guild.getApplicantList()) {
                sender.sendMessage(p + "\n");
            }
            sender.sendMessage("输入/gmgs app <ID> 通过玩家的入会申请");
        } else if (args.length != 2) {
            sendParameterError();
        } else {
            if (guild.getApplicantList().contains(args[1])) {
                if (guild.addMembers(args[1])) {
                    guild.getApplicantList().remove(args[1]);
                    sender.sendMessage(Message.INFO + "添加成功");
                } else {
                    sender.sendMessage(Message.ERROR + "成员已满");
                }
            } else {
                sender.sendMessage(Message.ERROR + "该玩家不在申请列表");
            }
        }
    }
    
    private void sendParameterError() {
        sender.sendMessage(Message.ERROR + "参数长度错误");
    }
    
    private void remove() {
        if (args.length != 2) {
            sendParameterError();
        } else {
            String playerID = args[1];
            if (playerID.equalsIgnoreCase(sender.getName())) {
                sender.sendMessage(Message.ERROR + "不能删除你自己");
            } else {
                if (guild.removeMembers(playerID)) {
                    sender.sendMessage(Message.INFO + "删除成功");
                } else {
                    sender.sendMessage(Message.INFO + "不存在该玩家");
                }
            }
        }
    }
    
    private void buyTpAll() {
        int flag = 0;
        if (args.length == 1) {
            flag = guildItem.buyTpTickets(guild, (Player) sender, 1);
        } else if (args.length == 2) {
            try {
                int n = Integer.parseInt(args[1]);
                flag = guildItem.buyTpTickets(guild, (Player) sender, n);
            } catch (NumberFormatException e) {
                sender.sendMessage(Message.ERROR + "请输入整数");
            }
        } else {
            sendParameterError();
        }
        switch (flag) {
            case 0:
                sender.sendMessage(Message.ERROR + "最多购买10份");
                break;
            case 1:
                sender.sendMessage(Message.ERROR + "资金不足");
                break;
            case 2:
                sender.sendMessage(Message.ERROR + "背包已满，请重试");
                break;
            case 3:
                sender.sendMessage(Message.INFO + "购买成功：公会召集令x5");
                break;
            default:
        }
    }
    
    private void clearMsg() {
        guild.clearMsgBoard();
    }
    
    private void advance() {
        if (args.length != 3) {
            sendParameterError();
        } else {
            String operate = args[1];
            String player = args[2];
            if (operate.equalsIgnoreCase("add")) {
                int flag = guild.addAdvancedMembers(player);
                switch (flag) {
                    case 0:
                        sender.sendMessage(Message.ERROR + "该玩家已存在于公会广场名单");
                        break;
                    case 1:
                        sender.sendMessage(Message.INFO + "增加成功");
                        break;
                    case 2:
                        sender.sendMessage(Message.ERROR + "已达到公会广场名单最大成员数");
                        break;
                    case 3:
                        sender.sendMessage(Message.ERROR + "该玩家不在你的公会");
                        break;
                    default:
                }
            } else if (operate.equalsIgnoreCase("remove")) {
                if (guild.getRemoveMemLimitFlag() > 0) {
                    sender.sendMessage(Message.INFO + "§c已超过今日删除玩家次数，请明天再试");
                } else if (guild.removeAdvancedMembers(player)) {
                    guild.addRemoveMemLimitFlag();
                    sender.sendMessage(Message.INFO + "删除成功");
                } else {
                    sender.sendMessage(Message.ERROR + "不存在该玩家");
                }
            }
        }
    }
    
    private void setName() {
        if (args.length != 2) {
            sendParameterError();
        } else {
            if (guild.isHasChangedName()) {
                sender.sendMessage(Message.ERROR + "名称已经设置，如需修改请使用更名卡");
                return;
            }
            if (args[1].contains("&") || args[1].contains("§")) {
                sender.sendMessage(Message.ERROR + "不可包含颜色代码");
            } else {
                guild.setName(args[1]);
                guild.setHasChangedName(true);
                sender.sendMessage(Message.INFO + "成功修改为" + args[1]);
            }
        }
    }
    
    private void levelUp() {
        if (args.length != 1) {
            sendParameterError();
        }
        if (guild.getLevel() >= 5) {
            sender.sendMessage(Message.INFO + "公会已达到满级");
            return;
        }
        int value = guild.levelUP();
        if (value == 1) {
            sender.sendMessage(Message.INFO + "公会积分不足，无法升级");
            sender.sendMessage("需要积分：" + guild.getLevel() * 5);
            sender.sendMessage("实际积分：" + guild.getPoints());
        }
        if (value == 2) {
            sender.sendMessage(Message.INFO + "公会资金不足，无法升级");
            sender.sendMessage("需要资金：" + (guild.getLevel() * 10 + 20));
            sender.sendMessage("实际资金：" + guild.getCash());
        }
        if (value == 3) {
            sender.sendMessage(Message.INFO + "公会升级成功，扣除公会资金" + guild.getLevel() * 5);
            sender.sendMessage("目前公会等级+1，为" + guild.getLevel());
            sender.sendMessage("目前公会最大人数+5，为" + guild.getMaxPlayers());
            sender.sendMessage("目前公会最大高级玩家数+2，为" + guild.getMaxAdvancedPlayers());
        }
    }
    
    private void res() {
        if (args.length == 1) {
            sender.sendMessage(Message.INFO + "§c缺少参数create或remove");
        } else if (args[1].equalsIgnoreCase("create")) {
            if (guild.getResidenceFLag()) {
                sender.sendMessage(Message.INFO + "请勿重复设置领地,领地 guild_" + guild.getId() + " 已存在");
            } else {
                guild.createResidence((Player) sender);
            }
        } else if (args[1].equalsIgnoreCase("remove")) {
            if (guild.getResidenceFLag()) {
                guild.removeResidence();
                sender.sendMessage(Message.INFO + "领地 guild_" + guild.getId() + " 删除成功");
            } else {
                sender.sendMessage(Message.INFO + "尚未设置领地");
                sender.sendMessage(Message.INFO + "请使用工具选点后输入创建指令");
            }
        }
    }
    
    private void warp() {
        if (args.length != 2) {
            sendParameterError();
        } else {
            if (args[1].equalsIgnoreCase("set")) {
                plugin.setWarp((Player) sender, guild.getId());
            }
            if (args[1].equalsIgnoreCase("del")) {
                plugin.delWarp(guild.getId());
            }
        }
    }
    
    private void position() {
        if (args.length != 4) {
            sendParameterError();
            sender.sendMessage("§a/gmg position set/remove <player> v/m  §2设置玩家为副会长/管理员");
        } else {
            if (args[1].equalsIgnoreCase("set")) {
                String p = args[2];
                if (!guild.hasPlayer(p)) {
                    sender.sendMessage(Message.ERROR + "公会无此玩家");
                    return;
                }
                if (args[3].equalsIgnoreCase("v")) {
                    if (guild.hasViceChairman(p)) {
                        sender.sendMessage(Message.ERROR + p + "已是副会长");
                        return;
                    }
                    if (guild.setViceChairman(p)) {
                        sender.sendMessage(Message.INFO + "设置成功");
                    } else {
                        sender.sendMessage(Message.ERROR + "副会长名额已满，最多2人");
                    }
                } else if (args[3].equalsIgnoreCase("m")) {
                    if (guild.hasManager(p)) {
                        sender.sendMessage(Message.ERROR + p + "已是管理员");
                        return;
                    }
                    if (guild.setManager(p)) {
                        sender.sendMessage(Message.INFO + "设置成功");
                    } else {
                        sender.sendMessage(Message.ERROR + "管理员名额已满，最多3人");
                    }
                } else {
                    sender.sendMessage(Message.ERROR + "参数错误:应为v/m");
                }
                return;
            }
            if (args[1].equalsIgnoreCase("remove")) {
                String p = args[2];
                if (!guild.hasPlayer(p)) {
                    sender.sendMessage(Message.ERROR + "公会无此玩家");
                    return;
                }
                if (args[3].equalsIgnoreCase("v")) {
                    if (guild.removeViceChairman(p)) {
                        sender.sendMessage(Message.INFO + "撤销成功");
                    } else {
                        sender.sendMessage(Message.ERROR + p + "不是副会长");
                    }
                } else if (args[3].equalsIgnoreCase("m")) {
                    if (guild.removeManager(p)) {
                        sender.sendMessage(Message.INFO + "撤销成功");
                    } else {
                        sender.sendMessage(Message.ERROR + p + "不是管理员");
                    }
                } else {
                    sender.sendMessage(Message.ERROR + "参数错误:应为v/m");
                }
            }
        }
    }
    
}
    
