package com.mcatk.guildmanager.command;

import com.mcatk.guildmanager.exceptions.ParaLengthException;
import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.Msg;
import com.mcatk.guildmanager.gui.GuildsGUI;
import com.mcatk.guildmanager.models.ApplicantsList;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.Member;
import com.mcatk.guildmanager.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildCommand implements CommandExecutor {

    private CommandSender sender;
    private String[] args;
    private Guild guild;

    // usage: /gmg gui|apply|tp|create|t|quit|offer|msg|memgui|msggui
    private void printHelp() {
        sender.sendMessage("§e------------公会帮助------------");
        sender.sendMessage("§a/gmg gui  §2公会列表");
        sender.sendMessage("§a/gmg apply <ID>  §2申请加入公会");
        sender.sendMessage("§a/gmg create <ID> §2创建公会（ID必须为英文）");
        sender.sendMessage("§a/gmg quit §2退出公会");
        sender.sendMessage("§a/gmg offer <AC点> §2捐助公会资金 1wAC = 1GuildCash");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.args = args;
        if (args.length == 0) {
            printHelp();
        } else {
            this.guild = SQLManager.getInstance().getPlayerGuild(sender.getName());
            try {
                onCommandWithoutGuild();
                //以下要求发送者在一个公会之中
                if (guild != null) {
                    onCommandWithGuild();
                }
            } catch (ParaLengthException e) {
                sender.sendMessage("参数错误");
            }
        }
        return true;
    }

    private void onCommandWithoutGuild() throws ParaLengthException {
        switch (args[0].toLowerCase()) {
            case "gui":
                new GuildsGUI().openGUI((Player) sender);
                break;
            case "apply":
                apply();
                break;
            case "create":
                create();
                break;
            default:
        }
    }

    private void onCommandWithGuild() throws ParaLengthException {
        switch (args[0].toLowerCase()) {
            case "offer":
                offer();
                break;
            case "quit":
                quit();
                break;
        }
    }

    private void apply() throws ParaLengthException {
        if (args.length != 2) {
            throw new ParaLengthException(2);
        }
        if (SQLManager.getInstance().getPlayerGuild(sender.getName()) != null) {
            sender.sendMessage(Msg.ERROR + "已有公会");
        } else {
            String guildID = args[1];
            Guild guild = SQLManager.getInstance().getGuild(guildID);
            if (guild == null) {
                sender.sendMessage(Msg.ERROR + "不存在公会");
            } else {
                if (ApplicantsList.getApplicantsList().getList(guildID).contains(sender.getName())) {
                    sender.sendMessage(Msg.ERROR + "今天已经申请过公会，明天再试");
                } else {
                    ApplicantsList.getApplicantsList().getList(guildID).add(sender.getName());
                    sender.sendMessage(Msg.INFO + "申请成功，等待通过");
                }
            }
        }
    }

    private void create() throws ParaLengthException {
        if (args.length != 2) {
            throw new ParaLengthException(2);
        }
        String guildID = args[1];
        if (!isAlphabet(args[1])) {
            sender.sendMessage(Msg.ERROR + "ID只能是小写字母");
            return;
        }
        Guild guild = SQLManager.getInstance().getPlayerGuild(sender.getName());
        if (guild != null) {
            sender.sendMessage(Msg.ERROR + "你已在公会" + guild.getGuildName());
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Msg.ERROR + "§c该指令只能由玩家发出");
            return;
        }
        if (GuildManager.getPlugin().takePlayerMoney((Player) sender, 1000000)) {
            SQLManager.getInstance().createGuild(guildID, sender.getName());
            SQLManager.getInstance().addMember(sender.getName(), guildID);
            sender.sendMessage(Msg.INFO + "创建成功");
            GuildManager.getPlugin().getLogger().info("玩家" + sender.getName() + "创建了公会" + args[1]);
        } else {
            sender.sendMessage(Msg.ERROR + "AC点不足！");
        }
    }

    private void offer() throws ParaLengthException {
        String p = sender.getName();
        if (args.length != 2) {
            throw new ParaLengthException(2);
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
            guild.setCash(guild.getCash() + n / 10000);
            //add contribution and check if is full.
            Member member = SQLManager.getInstance().getMember(sender.getName());
            if (member.getContribution() + n / 10000 > 100) {
                sender.sendMessage(Msg.INFO + "您的贡献值已满，无法继续增长");
            } else {
                member.setContribution(member.getContribution() + n / 10000);
            }
            sender.sendMessage(
                    Msg.INFO + "§a成功为" + guild.getGuildName() +
                            "§a捐赠" + n + "AC" + "折合为" + (n / 10000) + "公会资金"
            );
            GuildManager.getPlugin().getLogger().info(p + "捐献了" + n + "给" + guild.getGuildName());
            SQLManager.getInstance().saveGuild(guild);
        } else {
            sender.sendMessage(Msg.ERROR + "AC点不足！");
        }
    }

    private void quit() {
        if (guild.isManager(sender.getName())) {
            sender.sendMessage("请先撤销你的公会职务");
        } else {
            SQLManager.getInstance().removeMember(sender.getName(), guild.getId());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("res pset main.gh %s move remove", sender.getName()));
            sender.sendMessage(Msg.INFO + "退出公会" + guild.getGuildName());
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

}
