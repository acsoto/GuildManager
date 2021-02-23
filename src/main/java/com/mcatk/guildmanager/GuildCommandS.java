package com.mcatk.guildmanager;

import com.mcatk.guildmanager.msgs.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuildCommandS implements CommandExecutor {
    private GuildManager plugin;
    private Guilds guilds;
    
    GuildCommandS(GuildManager plugin, Guilds guilds) {
        this.plugin = plugin;
        this.guilds = guilds;
    }
    
    private GuildItem guildItem = new GuildItem();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Guild guild = guilds.getPlayersGuild(sender.getName());
        Player player = (Player) sender;
        String playerID = sender.getName();
        if (guild == null) {
            sender.sendMessage(Messages.errorPrefix + "您不在任何公会");
            return true;
        }
        if (!guild.isLeader(sender.getName())) {
            sender.sendMessage(Messages.errorPrefix + "您没有操作权限");
            return true;
        }
        final boolean isChairman = guild.getChairman().equalsIgnoreCase(playerID);
        final boolean isViceChairman = guild.getViceChairman().contains(playerID);
        final boolean isManager = guild.getManager().contains(playerID);
        
        if (args.length == 0) {
            printHelp(sender,guild,playerID);
            return true;
        }
        if (args[0].equalsIgnoreCase("app")) {
            if (args.length == 1) {
                sender.sendMessage("申请列表");
                for (String p :
                        guild.getApplicantList()) {
                    sender.sendMessage(p + "\n");
                }
                sender.sendMessage("输入/gmgs app <ID> 通过玩家申请");
            } else if (args.length != 2) {
                sender.sendMessage((Messages.errorPrefix + "参数错误"));
            } else {
                if (guild.getApplicantList().contains(args[1])) {
                    if (guild.addMembers(args[1])) {
                        sender.sendMessage(Messages.msgPrefix + "添加成功");
                    } else {
                        sender.sendMessage(Messages.errorPrefix + "成员已满");
                    }
                } else {
                    sender.sendMessage(Messages.errorPrefix + "该玩家不在申请列表");
                }
            }
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 2) {
                sender.sendMessage(Messages.msgPrefix + "§c缺少参数");
                return true;
            }
            if (args[1].equalsIgnoreCase(sender.getName())) {
                sender.sendMessage(Messages.errorPrefix + "不能添加你自己");
                return true;
            }
            Guild tempGuild = guilds.getPlayersGuild(args[1]);
            if (tempGuild != null) {
                sender.sendMessage(Messages.msgPrefix + "§该玩家已有公会" + tempGuild.getName());
                return true;
            }
            if (guild.addMembers(args[1])) {
                sender.sendMessage(Messages.msgPrefix + "增加成功");
            }
            else {
                sender.sendMessage(Messages.msgPrefix + "成员已满");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                sender.sendMessage(Messages.msgPrefix + "§c缺少参数");
                return true;
            }
            if (args[1].equalsIgnoreCase(sender.getName())) {
                sender.sendMessage(Messages.errorPrefix + "不能删除你自己");
                return true;
            }
            if (guild.getRemoveMemLimitFlag() > 0) {
                sender.sendMessage(Messages.msgPrefix + "§c已超过今日删除玩家次数，请明天再试");
                return true;
            } else if (guild.removeMembers(args[1])) {
                sender.sendMessage(Messages.msgPrefix + "删除成功");
                guild.addRemoveMemLimitFlag();
            } else {
                sender.sendMessage(Messages.msgPrefix + "不存在该玩家");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("buytpall")) {
            int flag;
            if (args.length == 1) {
                flag = guildItem.buyTpTickets(guild, player, 1);
            } else if (args.length == 2) {
                try {
                    int n = Integer.parseInt(args[1]);
                    flag = guildItem.buyTpTickets(guild, player, n);
                } catch (ClassCastException e) {
                    sender.sendMessage(Messages.errorPrefix + "请输入整数");
                    return true;
                }
            } else {
                sender.sendMessage(Messages.msgPrefix + "§c参数有误");
                return true;
            }
            switch (flag) {
                case 0:
                    sender.sendMessage(Messages.errorPrefix + "最多购买10份");
                    break;
                case 1:
                    sender.sendMessage(Messages.errorPrefix + "资金不足");
                    break;
                case 2:
                    sender.sendMessage(Messages.errorPrefix + "背包已满，请重试");
                    break;
                case 3:
                    sender.sendMessage(Messages.msgPrefix + "购买成功：公会召集令x5");
                    break;
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("tpall")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (!guildItem.oneOfThemIsTpTicket(item)) {
                sender.sendMessage(Messages.errorPrefix + "在你的背包里没有找到召集令");
                return true;
            }
            int amount = item.getAmount();
            amount--;
            item.setAmount(amount);
            plugin.tpAll(guild, player);
            sender.sendMessage(Messages.msgPrefix + "成功发起召集");
            return true;
        }
        if (args[0].equalsIgnoreCase("clearmsg")) {
            guild.clearMsgBoard();
        }
        //管理员指令止步于此
        if (isManager) {
            sender.sendMessage(Messages.errorPrefix + "§c指令输入错误或无权限");
            return true;
        }
        if (args[0].equalsIgnoreCase("adda")) {
            if (args.length < 2) {
                sender.sendMessage(Messages.msgPrefix + "§c缺少参数");
                return true;
            }
            String p = args[1];
            int flag = guild.addAdvancedMembers(p);
            switch (flag) {
                case 0:
                    sender.sendMessage(Messages.errorPrefix + "该玩家已存在于公会广场名单");
                    break;
                case 1:
                    sender.sendMessage(Messages.msgPrefix + "增加成功");
                    break;
                case 2:
                    sender.sendMessage(Messages.errorPrefix + "已达到公会广场名单最大成员数");
                    break;
                case 3:
                    sender.sendMessage(Messages.errorPrefix + "该玩家不在你的公会");
                    break;
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("removea")) {
            if (args.length < 2)
                sender.sendMessage(Messages.msgPrefix + "§c缺少参数");
            else if (guild.getRemoveMemLimitFlag() > 0) {
                sender.sendMessage(Messages.msgPrefix + "§c已超过今日删除玩家次数，请明天再试");
            } else if (guild.removeAdvancedMembers(args[1])) {
                guild.addRemoveMemLimitFlag();
                sender.sendMessage(Messages.msgPrefix + "删除成功");
            } else {
                sender.sendMessage(Messages.errorPrefix + "不存在该玩家");
            }
            return true;
        }
        
        //副会长指令止步于此
        if (isViceChairman) {
            sender.sendMessage(Messages.errorPrefix + "§c指令输入错误或无权限");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("setname")) {
            if (guild.isHasChangedName()) {
                sender.sendMessage(Messages.errorPrefix + "公会已设置名称,需要更名请使用公会更名卡");
            } else {
                if (args.length != 2) {
                    sender.sendMessage(Messages.errorPrefix + "参数错误");
                } else {
                    if (args[1].contains("&") || args[1].contains("§")) {
                        sender.sendMessage(Messages.errorPrefix + "不可包含颜色代码");
                    } else {
                        guild.setName(args[1]);
                        guild.setHasChangedName(true);
                        sender.sendMessage(Messages.msgPrefix + "成功修改为" + args[1]);
                    }
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("levelup")) {
            if (guild.getLevel() >= 5) {
                sender.sendMessage(Messages.msgPrefix + "公会已达到满级");
                return true;
            }
            int value = guild.levelUP();
            if (value == 1) {
                sender.sendMessage(Messages.msgPrefix + "公会积分不足，无法升级");
                sender.sendMessage("需要积分：" + guild.getLevel() * 5);
                sender.sendMessage("实际积分：" + guild.getPoints());
            }
            if (value == 2) {
                sender.sendMessage(Messages.msgPrefix + "公会资金不足，无法升级");
                sender.sendMessage("需要资金：" + (guild.getLevel() * 10 + 20));
                sender.sendMessage("实际资金：" + guild.getCash());
            }
            if (value == 3) {
                sender.sendMessage(Messages.msgPrefix + "公会升级成功，扣除公会资金" + guild.getLevel() * 5);
                sender.sendMessage("目前公会等级+1，为" + guild.getLevel());
                sender.sendMessage("目前公会最大人数+5，为" + guild.getMaxPlayers());
                sender.sendMessage("目前公会最大高级玩家数+2，为" + guild.getMaxAdvancedPlayers());
            }
            return true;
        }
        
        
        if (args[0].equalsIgnoreCase("res")) {
            if (args.length == 1)
                sender.sendMessage(Messages.msgPrefix + "§c缺少参数create或remove");
            else if (args[1].equalsIgnoreCase("create")) {
                if (guild.getResidenceFLag()) {
                    sender.sendMessage(Messages.msgPrefix + "请勿重复设置领地,领地 guild_" + guild.getId() + " 已存在");
                } else {
                    guild.createResidence((Player) sender);
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (guild.getResidenceFLag()) {
                    guild.removeResidence();
                    sender.sendMessage(Messages.msgPrefix + "领地 guild_" + guild.getId() + " 删除成功");
                } else {
                    sender.sendMessage(Messages.msgPrefix + "尚未设置领地");
                    sender.sendMessage(Messages.msgPrefix + "请使用工具选点后输入创建指令");
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("setwarp")) {
            plugin.setWarp((Player) sender, guild.getId());
        }
        if (args[0].equalsIgnoreCase("delwarp")) {
            plugin.delWarp(guild.getId());
        }
        
        
        if (args[0].equalsIgnoreCase("posset")) {
            if (args.length != 3) {
                sender.sendMessage(Messages.errorPrefix + "参数错误，请检查");
                sender.sendMessage("§a/gmg posset <player> v/m  §2设置玩家为副会长/管理员");
                return true;
            }
            String p = args[1];
            if (!guild.hasPlayer(p)) {
                sender.sendMessage(Messages.errorPrefix + "公会无此玩家");
                return true;
            }
            if (args[2].equalsIgnoreCase("v")) {
                if (guild.hasViceChairman(p)) {
                    sender.sendMessage(Messages.errorPrefix + p + "已是副会长");
                    return true;
                }
                if (guild.setViceChairman(p)) {
                    sender.sendMessage(Messages.msgPrefix + "设置成功");
                } else sender.sendMessage(Messages.errorPrefix + "副会长名额已满，最多2人");
            } else if (args[2].equalsIgnoreCase("m")) {
                if (guild.hasManager(p)) {
                    sender.sendMessage(Messages.errorPrefix + p + "已是管理员");
                    return true;
                }
                if (guild.setManager(p)) {
                    sender.sendMessage(Messages.msgPrefix + "设置成功");
                } else sender.sendMessage(Messages.errorPrefix + "管理员名额已满，最多3人");
            } else sender.sendMessage(Messages.errorPrefix + "参数错误:应为v/m");
            return true;
        }
        if (args[0].equalsIgnoreCase("posremove")) {
            if (args.length != 3) {
                sender.sendMessage(Messages.errorPrefix + "参数错误，请检查");
                sender.sendMessage("§a/gmg posremove <player> v/m §2撤销玩家的副会长/管理员");
                return true;
            }
            String p = args[1];
            if (!guild.hasPlayer(p)) {
                sender.sendMessage(Messages.errorPrefix + "公会无此玩家");
                return true;
            }
            if (args[2].equalsIgnoreCase("v")) {
                if (guild.removeViceChairman(p))
                    sender.sendMessage(Messages.msgPrefix + "撤销成功");
                else sender.sendMessage(Messages.errorPrefix + p + "不是副会长");
            } else if (args[2].equalsIgnoreCase("m")) {
                if (guild.removeManager(p))
                    sender.sendMessage(Messages.msgPrefix + "撤销成功");
                else sender.sendMessage(Messages.errorPrefix + p + "不是管理员");
            } else sender.sendMessage(Messages.errorPrefix + "参数错误:应为v/m");
            return true;
        }
        if (args[0].equalsIgnoreCase("setally")) {
            if (args.length != 2) {
                sender.sendMessage(Messages.errorPrefix + "参数错误，请检查");
                return true;
            }
            if (guilds.hasGuild(args[1])) {
                guild.setAlly(args[1]);
            } else {
                sender.sendMessage(Messages.errorPrefix + "无此公会");
            }
            return true;
        }
        //////////////////////////
        sender.sendMessage(Messages.errorPrefix + "§c指令输入错误");
        return false;
    }
    
    void printHelp(CommandSender sender,Guild guild, String playerID){
        final boolean isChairman = guild.getChairman().equalsIgnoreCase(playerID);
        final boolean isViceChairman = guild.getViceChairman().contains(playerID);
        final boolean isManager = guild.getManager().contains(playerID);
        sender.sendMessage("§e------------公会操作帮助------------");
        if (isChairman) {
            sender.sendMessage("§a/gmgs setname <name>  §2公会名称设置");
            sender.sendMessage("§a/gmgs levelup  §2公会升级");
            sender.sendMessage("§a/gmgs res create §2公会圈地(工具选点后输入该指令)");
            sender.sendMessage("§a/gmgs res remove  §2删除公会领地");
            sender.sendMessage("§a/gmgs setwarp  §2设置公会领地标");
            sender.sendMessage("§a/gmgs delwarp  §2删除公会领地标");
            sender.sendMessage("§a/gmgs posset <player> v/m  §2设置玩家为副会长/管理员");
            sender.sendMessage("§a/gmgs posremove <player> v/m §2撤销玩家的副会长/管理员");
            sender.sendMessage("§a/gmgs setally <guildID> §2设置伙伴公会");
        }
        if (isChairman || isViceChairman) {
            sender.sendMessage("§a/gmgs adda <player>  §2增加玩家到公会广场名单");
            sender.sendMessage("§a/gmgs removea <player>  §2从公会广场名单删除玩家");
        }
        sender.sendMessage("§a/gmgs app  §2查看公会加入申请");
//            sender.sendMessage("§a/gmgs add <player>  §2增加玩家");
        sender.sendMessage("§a/gmgs remove <player>  §2删除玩家");
        sender.sendMessage("§a/gmgs buytpall (num) §2购买公会召集令");
        sender.sendMessage("§a/gmgs tpall  §2发起召集");
        sender.sendMessage("§a/gmgs clearmsg  §2清空留言板");
    }
}