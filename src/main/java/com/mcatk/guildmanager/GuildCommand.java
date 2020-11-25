package com.mcatk.guildmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuildCommand implements CommandExecutor {
    final String MsgPrefix = "§6§l公会系统 §7>>> §a";
    final String ErrorPrefix = "§4§l错误 §7>>> §c";
    GuildManager plugin = GuildManager.plugin;
    GuildItem guildItem = new GuildItem();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0){
            sender.sendMessage("§e------------普通帮助------------");
            sender.sendMessage("§a/gmg list  §2公会列表");
            sender.sendMessage("§a/gmg check <ID>  §2查看公会详情");
            sender.sendMessage("§a/gmg tp <guild> §2传送到某公会主城");
            sender.sendMessage("§a/gmg t §2传送到自己的公会主城");
            sender.sendMessage("§a/gmg s §2查看公会状态");
            sender.sendMessage("§a/gmg mems §2查看公会成员列表");
            sender.sendMessage("§a/gmg amems §2查看公会名高级成员列表");
            sender.sendMessage("§a/gmg offer <AC点> §2捐助公会资金 1wAC = 1GuildCash");
            sender.sendMessage("§a/gmg create <ID> §2创建公会（ID必须为英文）");
            if(plugin.getChairmansGuild(sender.getName())!=null) {
                sender.sendMessage("§e------------会长帮助------------");
                sender.sendMessage("§a/gmg setname <name>  §2公会名称设置");
                sender.sendMessage("§a/gmg levelup  §2公会升级");
                sender.sendMessage("§a/gmg add <player>  §2增加玩家");
                sender.sendMessage("§a/gmg remove <player>  §2删除玩家");
                sender.sendMessage("§a/gmg adda <player>  §2增加玩家到公会广场名单");
                sender.sendMessage("§a/gmg removea <player>  §2从公会广场名单删除玩家");
                sender.sendMessage("§a/gmg res create §2公会圈地(工具选点后输入该指令)");
                sender.sendMessage("§a/gmg res remove  §2删除公会领地");
                sender.sendMessage("§a/gmg setwarp  §2设置公会领地标");
                sender.sendMessage("§a/gmg delwarp  §2删除公会领地标");
                sender.sendMessage("§a/gmg buytpall  §2购买公会召集令");
                sender.sendMessage("§a/gmg posset <player> v/m  §2设置玩家为副会长/管理员");
                sender.sendMessage("§a/gmg posremove <player> v/m §2撤销玩家的副会长/管理员");
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("list")){
            GuildManager.plugin.listGuilds(sender);
            return true;
        }
        if(args[0].equalsIgnoreCase("check")){
            if(args.length==1)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(GuildManager.plugin.hasGuild(args[1])){
                Guild guild= GuildManager.plugin.getGuild(args[1]);
                if(guild==null){
                    sender.sendMessage(ErrorPrefix+"不存在此公会");
                    return true;
                }
                sender.sendMessage(guild.checkStatus());
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
        if(args[0].equalsIgnoreCase("create")){
            if(args.length!=2){
                sender.sendMessage(ErrorPrefix+"参数错误");
                return true;
            }
            if(!isAlphabet(args[1])){
                sender.sendMessage(ErrorPrefix+"ID只能是小写字母");
                return false;
            }
            Guild guild = plugin.getPlayersGuild(sender.getName());
            if(guild!=null){
                sender.sendMessage(ErrorPrefix+"你已在公会"+guild.getName());
                return true;
            }
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            if(plugin.takePlayerMoney((Player)sender,plugin.getReqCreateGuildMoney())){
                plugin.newGuild(args[1],sender.getName());
                sender.sendMessage(MsgPrefix+"创建成功");
                plugin.logInfo("玩家"+sender.getName()+"创建了公会"+args[1]);
            }
            else sender.sendMessage(ErrorPrefix+"AC点不足！");
            return true;
        }
        //以下要求发送者在一个公会之中
        Guild guild = plugin.getPlayersGuild(sender.getName());
        if(guild==null){
            sender.sendMessage(MsgPrefix+"§c你不在任何公会");
            return true;
        }
        if(args[0].equalsIgnoreCase("t")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            plugin.tpGuild(guild.getName(),sender.getName());
            sender.sendMessage(MsgPrefix+"§a传送成功");
            return true;
        }
        if(args[0].equalsIgnoreCase("offer")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ErrorPrefix+"§c该指令只能由玩家发出");
                return true;
            }
            String p =sender.getName();
            if(args.length!=2) {
                sender.sendMessage(MsgPrefix + "§c参数错误");
                return true;
            }
            if(!isLegalMoney(args[1])){
                sender.sendMessage(MsgPrefix+"§c必须是整数！");
                return true;
            }
            int n;
            try{
                n = Integer.parseInt(args[1]);
            }catch (ClassCastException e){
                sender.sendMessage(MsgPrefix+"§c必须是整数！");
                return true;
            }
            if(!isLegalMoneyToCash(n)){
                sender.sendMessage(MsgPrefix+"§c必须是10000的整数倍！");
                return true;
            }
            if(plugin.takePlayerMoney((Player) sender,n)) {
            guild.addCash(n/10000);
            //add contribution and check if is full.
                if(!guild.getMember(p).addContribution(n/10000))
                    sender.sendMessage(MsgPrefix+"您的贡献值已满，无法继续增长");
                sender.sendMessage(MsgPrefix+"§a成功为"+guild.getName()+"§a捐赠"+n+"AC"+"折合为"+(n/10000)+"公会资金");
            }
            else sender.sendMessage(ErrorPrefix+"AC点不足！");
            return true;
        }
        if(args[0].equalsIgnoreCase("s")){
            sender.sendMessage(guild.checkStatus());
            return true;
        }
        if(args[0].equalsIgnoreCase("name")){
            sender.sendMessage(guild.checkGuildName());
            return true;
        }
        if(args[0].equalsIgnoreCase("chairman")){
            sender.sendMessage(guild.checkChairman());
            return true;
        }
        if(args[0].equalsIgnoreCase("cash")){
            sender.sendMessage(guild.checkCash());
            return true;
        }
        if(args[0].equalsIgnoreCase("points")){
            sender.sendMessage(guild.checkPoints());
            return true;
        }
        if(args[0].equalsIgnoreCase("mems")){
            sender.sendMessage(guild.checkMemSize());
            sender.sendMessage(guild.listMembers());
            return true;
        }
        if(args[0].equalsIgnoreCase("amems")){
            sender.sendMessage(guild.checkAdvancedMemSize());
            sender.sendMessage(guild.listAdvancedMembers());
            return true;
        }
        //会长操作
        guild = plugin.getChairmansGuild(sender.getName());
        if(guild==null){
            sender.sendMessage(MsgPrefix+"§c你不是会长");
            return true;
        }
        Player player = (Player) sender;
        //以下为会长操作
        if(args[0].equalsIgnoreCase("levelup")){
            if(guild.getLevel()>=5){
                sender.sendMessage(MsgPrefix+"公会已达到满级");
                return true;
            }
            int value = guild.levelUP();
            if(value==1) {
                sender.sendMessage(MsgPrefix + "公会积分不足，无法升级");
                sender.sendMessage("需要积分："+guild.getLevel()*5);
                sender.sendMessage("实际积分："+guild.getPoints());
            }
            if(value==2) {
                sender.sendMessage(MsgPrefix + "公会资金不足，无法升级");
                sender.sendMessage("需要资金："+(guild.getLevel()*10+20));
                sender.sendMessage("实际资金："+guild.getCash());
            }
            if(value==3){
                sender.sendMessage(MsgPrefix+"公会升级成功，扣除公会资金"+guild.getLevel()*5);
                sender.sendMessage("目前公会等级+1，为"+guild.getLevel());
                sender.sendMessage("目前公会最大人数+5，为"+guild.getMaxPlayers());
                sender.sendMessage("目前公会最大高级玩家数+2，为"+guild.getMaxAdvancedPlayers());
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("add")){
            if(args.length<2) {
                sender.sendMessage(MsgPrefix + "§c缺少参数");
                return true;
            }
            if(args[1].equalsIgnoreCase(sender.getName())){
                sender.sendMessage(ErrorPrefix+"不能添加你自己");
                return true;
            }
            Guild tempGuild =  plugin.getPlayersGuild(args[1]);
            if(tempGuild!=null){
                sender.sendMessage(MsgPrefix + "§该玩家已有公会"+tempGuild.getName());
                return true;
            }
            if(guild.addMembers(args[1]))
                sender.sendMessage(MsgPrefix+"增加成功");
            else
                sender.sendMessage(MsgPrefix+"成员已满");
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")){
            if(args.length<2) {
                sender.sendMessage(MsgPrefix + "§c缺少参数");
                return true;
            }
            if(args[1].equalsIgnoreCase(sender.getName())){
                sender.sendMessage(ErrorPrefix + "不能删除你自己");
                return true;
            }
            if(guild.getRemoveMemLimitFlag()>0){
                sender.sendMessage(MsgPrefix + "§c已超过今日删除玩家次数，请明天再试");
                return true;
            }
            else if(guild.removeMembers(args[1])) {
                sender.sendMessage(MsgPrefix + "删除成功");
                guild.addRemoveMemLimitFlag();
            }
            else
                sender.sendMessage(MsgPrefix + "不存在该玩家");
            return true;
        }
        if(args[0].equalsIgnoreCase("adda")){
            if(args.length<2) {
                sender.sendMessage(MsgPrefix + "§c缺少参数");
                return true;
            }
            String p = args[1];
            int flag = guild.addAdvancedMembers(p);
            switch (flag){
                case 0:
                    sender.sendMessage(ErrorPrefix + "该玩家已存在于公会广场名单");
                    break;
                case 1:
                    sender.sendMessage(MsgPrefix + "增加成功");
                    break;
                case 2:
                    sender.sendMessage(ErrorPrefix + "已达到公会广场名单最大成员数");
                    break;
                case 3:
                    sender.sendMessage(ErrorPrefix + "该玩家不在你的公会");
                    break;
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("removea")){
            if(args.length<2)
                sender.sendMessage(MsgPrefix+"§c缺少参数");
            else if(guild.getRemoveMemLimitFlag()>0){
                sender.sendMessage(MsgPrefix + "§c已超过今日删除玩家次数，请明天再试");
            }
            else if(guild.removeAdvancedMembers(args[1])) {
                guild.addRemoveMemLimitFlag();
                sender.sendMessage(MsgPrefix + "删除成功");
            }
            else
                sender.sendMessage(ErrorPrefix + "不存在该玩家");
            return true;
        }
        if (args[0].equalsIgnoreCase("setname")){
            if(args.length<2){
                sender.sendMessage(ErrorPrefix+"§c缺少参数");
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
                    sender.sendMessage(MsgPrefix + "请勿重复设置领地,领地 guild_"+guild.getID()+" 已存在");
                }
                else {
                    guild.createResidence((Player) sender);
                }
            }
            else if(args[1].equalsIgnoreCase("remove")){
                if(guild.getResidenceFLag()){
                    guild.removeResidence();
                    sender.sendMessage(MsgPrefix + "领地 guild_"+guild.getID()+" 删除成功");
                }
                else {
                    sender.sendMessage(MsgPrefix + "尚未设置领地");
                    sender.sendMessage(MsgPrefix + "请使用工具选点后输入创建指令");
                }
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("setwarp")){
            plugin.setWarp((Player)sender,guild.getID());
        }
        if(args[0].equalsIgnoreCase("delwarp")){
            plugin.delWarp(guild.getID());
        }
        if(args[0].equalsIgnoreCase("buytpall")){
            int flag;
            if(args.length==1){
                flag = guildItem.buyTpTickets(guild,player,1);
            }
            else if(args.length==2){
                try {
                    int n = Integer.parseInt(args[1]);
                    flag = guildItem.buyTpTickets(guild,player,n);
                }catch (ClassCastException e){
                    sender.sendMessage(ErrorPrefix+"请输入整数");
                    return true;
                }
            }
            else {
                sender.sendMessage(MsgPrefix + "§c参数有误");
                return true;
            }
            switch (flag){
                case 0:
                    sender.sendMessage(ErrorPrefix + "最多购买10份");
                    break;
                case 1:
                    sender.sendMessage(ErrorPrefix + "资金不足");
                    break;
                case 2:
                    sender.sendMessage(ErrorPrefix + "背包已满，请重试");
                    break;
                case 3:
                    sender.sendMessage(MsgPrefix + "购买成功：公会召集令x5");
                    break;
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("tpall")){
            ItemStack item = player.getInventory().getItemInMainHand();
            if(!guildItem.oneOfThemIsTpTicket(item)){
                sender.sendMessage(ErrorPrefix+"在你的背包里没有找到召集令");
                return true;
            }
            int amount = item.getAmount();
            amount--;
            item.setAmount(amount);
            plugin.tpAll(guild,player);
            sender.sendMessage(MsgPrefix+"成功发起召集");
            return true;
        }
        if(args[0].equalsIgnoreCase("posset")){
            if(args.length!=3){
                sender.sendMessage(ErrorPrefix+"参数错误，请检查");
                sender.sendMessage("§a/gmg posset <player> v/m  §2设置玩家为副会长/管理员");
                return true;
            }
            String p = args[1];
            if(!guild.hasPlayer(p)){
                sender.sendMessage(ErrorPrefix+"公会无此玩家");
                return true;
            }
            if (args[2].equalsIgnoreCase("v")){
                if(guild.hasViceChairman(p)){
                    sender.sendMessage(ErrorPrefix+p+"已是副会长");
                    return true;
                }
                if(guild.setViceChairman(p)){
                    sender.sendMessage(MsgPrefix+"设置成功");
                }
                else sender.sendMessage(ErrorPrefix+"副会长名额已满，最多2人");
            }
            else if (args[2].equalsIgnoreCase("m")){
                if(guild.hasManager(p)){
                    sender.sendMessage(ErrorPrefix+p+"已是管理员");
                    return true;
                }
                if(guild.setManager(p)){
                    sender.sendMessage(MsgPrefix+"设置成功");
                }
                else sender.sendMessage(ErrorPrefix+"管理员名额已满，最多3人");
            }
            else sender.sendMessage(ErrorPrefix+"参数错误:应为v/m");
            return true;
        }
        if(args[0].equalsIgnoreCase("posremove")){
            if(args.length!=3){
                sender.sendMessage(ErrorPrefix+"参数错误，请检查");
                sender.sendMessage("§a/gmg posremove <player> v/m §2撤销玩家的副会长/管理员");
                return true;
            }
            String p = args[1];
            if(!guild.hasPlayer(p)){
                sender.sendMessage(ErrorPrefix+"公会无此玩家");
                return true;
            }
            if(args[2].equalsIgnoreCase("v")){
                if(guild.removeViceChairman(p))
                    sender.sendMessage(MsgPrefix+"撤销成功");
                else sender.sendMessage(ErrorPrefix+p+"不是副会长");
            }
            else if(args[2].equalsIgnoreCase("m")){
                if(guild.removeManager(p))
                    sender.sendMessage(MsgPrefix+"撤销成功");
                else sender.sendMessage(ErrorPrefix+p+"不是管理员");
            }
            else sender.sendMessage(ErrorPrefix+"参数错误:应为v/m");
            return true;
        }
        /////////////////////////////////
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

    Boolean isLegalMoneyToCash(int money){
        return (money % 10000) == 0;
    }

    Boolean isAlphabet(String str){
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(!(c>='a'&&c<='z'))
                return false;
        }
        return true;
    }
}
