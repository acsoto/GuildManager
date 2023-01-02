package com.mcatk.guildmanager.command;

import com.mcatk.guildmanager.GuildItem;
import com.mcatk.guildmanager.Msg;
import com.mcatk.guildmanager.Operation;
import com.mcatk.guildmanager.ServerCmd;
import com.mcatk.guildmanager.exceptions.ParaLengthException;
import com.mcatk.guildmanager.models.ApplicantsList;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.GuildBasicInfo;
import com.mcatk.guildmanager.models.Member;
import com.mcatk.guildmanager.sql.SQLManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildCommandS implements CommandExecutor {
    private GuildItem guildItem;
    private CommandSender sender;
    private String[] args;
    private Guild guild;

    public GuildCommandS() {
        this.guildItem = new GuildItem();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.args = args;
        this.guild = SQLManager.getInstance().getPlayerGuild(sender.getName());
        if (guild == null) {
            sender.sendMessage(Msg.ERROR + "您不在任何公会");
            return true;
        }
        if (!guild.isManager(sender.getName())) {
            sender.sendMessage(Msg.ERROR + "您没有操作该公会的权限");
            return true;
        }
        if (args.length == 0) {
            printHelp();
            return true;
        }
        try {
            onCommandChairman();
        } catch (ParaLengthException e) {
            sender.sendMessage(String.valueOf(e));
        }
        SQLManager.getInstance().saveGuild(guild);
        return true;
    }

    private void onCommandChairman() throws ParaLengthException {
        switch (args[0].toLowerCase()) {
            case "app":
                app();
                break;
            case "remove":
                remove();
                break;
            case "buytpall":
                buyTpAll();
                break;
            case "advance":
                advance();
                break;
            case "setname":
                setName();
                break;
            case "res":
                res();
                break;
            case "warp":
                warp();
                break;
            case "setvice1":
                if (SQLManager.getInstance().getGuildMembers(guild.getId()).contains(args[1])) {
                    guild.setViceChairman1(args[1]);
                }
                break;
            case "setvice2":
                if (SQLManager.getInstance().getGuildMembers(guild.getId()).contains(args[1])) {
                    guild.setViceChairman2(args[1]);
                }
                break;
            case "buy":
                buy();
            default:
        }
    }

    // usage: /gmgs setname|levelup|res create|res remove|warp set|warp del|position set|position removve|setally|advance add|advance remove|app|remove|buytpall|tpall|clearmsg
    void printHelp() {
        sender.sendMessage("§e------------公会操作帮助------------");
        sender.sendMessage("§a/gmgs setname <name>  §2公会名称设置");
        sender.sendMessage("§a/gmgs levelup  §2公会升级");
        sender.sendMessage("§a/gmgs res create §2公会圈地(工具选点后输入该指令)");
        sender.sendMessage("§a/gmgs res remove  §2删除公会领地");
        sender.sendMessage("§a/gmgs warp set/del  §2设置/删除公会领地标");
        sender.sendMessage("§a/gmgs setvice1 <player>  §2设置玩家为副会长1");
        sender.sendMessage("§a/gmgs setvice2 <player>  §2设置玩家为副会长2");
        sender.sendMessage("§a/gmgs advance add <player>  §2增加玩家到公会广场名单");
        sender.sendMessage("§a/gmgs advance remove <player>  §2从公会广场名单删除玩家");
        sender.sendMessage("§a/gmgs app  §2查看公会加入申请");
        sender.sendMessage("§a/gmgs remove <player>  §2删除玩家");
        sender.sendMessage("§a/gmgs buytpall (num) §2购买公会召集令");
        sender.sendMessage("§a/gmgs tpall  §2发起召集");
        sender.sendMessage("§a/gmgs buy nametag");
    }

    void app() throws ParaLengthException {
        if (args.length == 1) {
            sender.sendMessage("申请列表:");
            for (String p : ApplicantsList.getApplicantsList().getList(guild.getId())) {
                sender.sendMessage(p + "\n");
            }
            sender.sendMessage("输入/gmgs app <ID> 通过玩家的入会申请");
        } else if (args.length != 2) {
            throw new ParaLengthException(2);
        } else {
            if (ApplicantsList.getApplicantsList().getList(guild.getId()).contains(args[1])) {
                if (SQLManager.getInstance().getGuildAdvancedMembers(guild.getId()).size() < GuildBasicInfo.getMaxPlayer(guild.getLevel())) {
                    ApplicantsList.getApplicantsList().getList(guild.getId()).remove(args[1]);
                    SQLManager.getInstance().addMember(args[1], guild.getId());
                    sender.sendMessage(Msg.INFO + "添加成功");
                } else {
                    sender.sendMessage(Msg.ERROR + "成员已满");
                }
            } else {
                sender.sendMessage(Msg.ERROR + "该玩家不在申请列表");
            }
        }
    }

    private void sendParameterError() {
        sender.sendMessage(Msg.ERROR + "参数长度错误");
    }

    private void remove() throws ParaLengthException {
        if (args.length != 2) {
            throw new ParaLengthException(2);
        } else {
            String playerID = args[1];
            if (playerID.equalsIgnoreCase(sender.getName())) {
                sender.sendMessage(Msg.ERROR + "不能删除你自己");
            } else {
                SQLManager.getInstance().removeMember(playerID, guild.getId());
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
                sender.sendMessage(Msg.ERROR + "请输入整数");
            }
        } else {
            sendParameterError();
        }
        switch (flag) {
            case 0:
                sender.sendMessage(Msg.ERROR + "最多购买10份");
                break;
            case 1:
                sender.sendMessage(Msg.ERROR + "资金不足");
                break;
            case 2:
                sender.sendMessage(Msg.ERROR + "背包已满，请重试");
                break;
            case 3:
                sender.sendMessage(Msg.INFO + "购买成功：公会召集令x5");
                break;
            default:
        }
    }

    private void advance() throws ParaLengthException {
        if (args.length != 3) {
            throw new ParaLengthException(3);
        } else {
            String operate = args[1];
            String player = args[2];
            if (operate.equalsIgnoreCase("add")) {
                if (SQLManager.getInstance().getGuildAdvancedMembers(guild.getId()).size() < GuildBasicInfo.getMaxAdvancedPlayer(guild.getLevel())) {
                    if (SQLManager.getInstance().getGuildMembers(guild.getId()).contains(player)) {
                        Member member = SQLManager.getInstance().getMember(player);
                        member.setAdvanced(true);
                        SQLManager.getInstance().saveMember(member);
                        sender.sendMessage(Msg.INFO + "增加成功");
                    } else {
                        sender.sendMessage(Msg.ERROR + "不是公会成员");
                    }
                } else {
                    sender.sendMessage(Msg.ERROR + "已达到公会广场名单最大成员数");
                }
            } else if (operate.equalsIgnoreCase("remove")) {
                if (SQLManager.getInstance().getGuildMembers(guild.getId()).contains(player)) {
                    Member member = SQLManager.getInstance().getMember(player);
                    member.setAdvanced(false);
                    SQLManager.getInstance().saveMember(member);
                } else {
                    sender.sendMessage(Msg.ERROR + "不是公会成员");
                }
            }
        }
    }

    private void setName() throws ParaLengthException {
        if (args.length != 2) {
            throw new ParaLengthException(2);
        } else {
            if (guild.getHasChangedName()) {
                sender.sendMessage(Msg.ERROR + "名称已经设置，如需修改请使用更名卡");
                return;
            }
            if (args[1].contains("&") || args[1].contains("§")) {
                sender.sendMessage(Msg.ERROR + "不可包含颜色代码");
            } else {
                guild.setGuildName(args[1]);
                guild.setHasChangedName(true);
                sender.sendMessage(Msg.INFO + "成功修改为" + args[1]);
            }
        }
    }

//    private void levelUp() {
//        if (guild.getLevel() >= 5) {
//            sender.sendMessage(Msg.INFO + "公会已达到满级");
//            return;
//        }
//        int value = guild.levelUP();
//        if (value == 1) {
//            sender.sendMessage(Msg.INFO + "公会积分不足，无法升级");
//            sender.sendMessage("需要积分：" + guild.getLevel() * 5);
//            sender.sendMessage("实际积分：" + guild.getPoints());
//        }
//        if (value == 2) {
//            sender.sendMessage(Msg.INFO + "公会资金不足，无法升级");
//            sender.sendMessage("需要资金：" + (guild.getLevel() * 10 + 20));
//            sender.sendMessage("实际资金：" + guild.getCash());
//        }
//        if (value == 3) {
//            sender.sendMessage(Msg.INFO + "公会升级成功，扣除公会资金" + guild.getLevel() * 5);
//            sender.sendMessage("目前公会等级+1，为" + guild.getLevel());
//            sender.sendMessage("目前公会最大人数+5，为" + guild.getMaxPlayers());
//            sender.sendMessage("目前公会最大高级玩家数+2，为" + guild.getMaxAdvancedPlayers());
//        }
//    }

    private void res() {
        if (args.length == 1) {
            sender.sendMessage(Msg.INFO + "§c缺少参数create或remove");
        } else if (args[1].equalsIgnoreCase("create")) {
            if (guild.getResidenceFLag()) {
                sender.sendMessage(Msg.INFO + "请勿重复设置领地,领地 guild_" + guild.getId() + " 已存在");
            } else {
                new ServerCmd().createResidence(guild.getId(), (Player) sender);
            }
        } else if (args[1].equalsIgnoreCase("remove")) {
            if (guild.getResidenceFLag()) {
                new ServerCmd().sendConsoleCmd("resadmin remove guild_" + guild.getId());
                new ServerCmd().sendConsoleCmd("resadmin confirm");
                guild.setResidenceFLag(false);
                sender.sendMessage(Msg.INFO + "领地 guild_" + guild.getId() + " 删除成功");
            } else {
                sender.sendMessage(Msg.INFO + "尚未设置领地");
                sender.sendMessage(Msg.INFO + "请使用工具选点后输入创建指令");
            }
        }
    }

    private void warp() throws ParaLengthException {
        if (args.length != 2) {
            throw new ParaLengthException(2);
        } else {
            if (args[1].equalsIgnoreCase("set")) {
                new Operation().setWarp((Player) sender, guild);
            }
            if (args[1].equalsIgnoreCase("del")) {
                new Operation().delWarp(guild);
            }
        }
    }

    private void buy() throws ParaLengthException {
        if (args.length != 2) {
            throw new ParaLengthException(2);
        }
        if (args[1].equalsIgnoreCase("nametag")) {
            if (guild.getCash() < 10) {
                sender.sendMessage("§c公会资金不足");
                return;
            }
            guild.setCash(guild.getCash() - 10);
            guild.setHasChangedName(false);
            sender.sendMessage("§a更名卡购买成功，你可以通过/gmgs setname 来修改公会名称");
        }
    }

}
    
