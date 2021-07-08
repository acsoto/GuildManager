package com.mcatk.guildmanager;

import java.util.ArrayList;
import java.util.HashMap;

public class Guild {
    private ArrayList<String> msgBoard; //留言板
    //需要保存的成员变量
    private String id; //标记ID
    private String guildName; //公会名
    private String chairMan; //会长
    private ArrayList<String> viceChairman; //副会长
    private ArrayList<String> manager; //管理员
    private int level; //等级
    private int maxPlayers; //最大成员数
    private int advancedPlayers; //高级成员数
    private int maxAdvancedPlayers; //最大高级成员数
    private int points; //积分
    private int removeMemLimitFlag; //成员删除限制flag
    private boolean residenceFLag; //是否拥有领地
    private HashMap<String, Member> members; //成员
    private int cash; //资金
    private int reposSize;
    private boolean hasChangedName;
    private ArrayList<String> applicantList;
    
    //构造方法
    public Guild(String id, String player) {
        this.id = id;
        this.guildName = id;
        this.chairMan = player;
        this.level = 1;
        this.maxPlayers = 10;
        this.maxAdvancedPlayers = 5;
        this.reposSize = 9;
        this.msgBoard = new ArrayList<>();
        this.viceChairman = new ArrayList<>();
        this.manager = new ArrayList<>();
        this.members = new HashMap<>();
        addMembers(player);
        this.applicantList = new ArrayList<>();
    }
    
    //成员变量增删查改
    public String getId() {
        return id;
    }
    
    public void setName(String name) {
        guildName = GuildManager.getPlugin().colorFormat(name);
    }
    
    public String getName() {
        return guildName;
    }
    
    public String getChairman() {
        return chairMan;
    }
    
    public boolean addViceChairman(String p) {
        if (viceChairman.size() >= 2) {
            return false;
        }
        viceChairman.add(p);
        return true;
    }
    
    public boolean removeViceChairman(String p) {
        return viceChairman.remove(p);
    }
    
    public boolean hasViceChairman(String p) {
        return viceChairman.contains(p);
    }
    
    public boolean addManager(String p) {
        if (manager.size() >= 3) {
            return false;
        }
        manager.add(p);
        return true;
    }
    
    public boolean removeManager(String p) {
        return manager.remove(p);
    }
    
    public boolean hasManager(String p) {
        return manager.contains(p);
    }
    
    public boolean hasChairman(String p) {
        return p.equals(chairMan) || viceChairman.contains(p);
    }
    
    public int levelUP() {
        int reqPoints = level * 5;
        int reqCash = level * 10 + 20;
        if (points < reqPoints) {
            return 1;
        }
        if (cash < reqCash) {
            return 2;
        }
        cash -= reqCash;
        level++;
        maxPlayers += 5;
        maxAdvancedPlayers += 2;
        return 3;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getPlayerSize() {
        return members.size();
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public int getMaxAdvancedPlayers() {
        return maxAdvancedPlayers;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void addCash(int n) {
        cash += n;
    }
    
    public boolean takeCash(int n) {
        if ((cash - n) < 0) {
            return false;
        }
        cash -= n;
        return true;
    }
    
    public void addPoints(int n) {
        points += n;
    }
    
    public boolean takePoints(int n) {
        if ((points - n) < 0) {
            return false;
        }
        points -= n;
        return true;
    }
    
    public int getCash() {
        return cash;
    }
    
    //公会成员操作
    public boolean addMembers(String p) {
        Member member = new Member(p);
        if (members.size() <= maxPlayers) {
            members.put(p, member);
            return true;
        } else {
            return false;
        }
    }
    
    //删除成员并且删除其权限和职位并保存
    public boolean removeMembers(String p) {
        Member member = members.remove(p);
        if (member != null) {
            removeGuildSquarePerm(p);
            removeViceChairman(p);
            removeManager(p);
            return true;
        } else {
            return false;
        }
    }
    
    //设置玩家为高级成员并保存
    //判断是否满员
    public int addAdvancedMembers(String p) {
        Member member = members.get(p);
        if (member != null) {
            if (member.isAdvanced()) {
                return 0;
            } else if (advancedPlayers < maxAdvancedPlayers) {
                member.setAdvanced(true);
                advancedPlayers++;
                giveGuildSquarePerm(p);
                return 1;
            } else {
                return 2;
            }
        }
        return 3;
    }
    
    //删除玩家的高级权限
    public boolean removeAdvancedMembers(String p) {
        Member member = members.get(p);
        if (member != null) {
            removeGuildSquarePerm(p);
            return true;
        } else {
            return false;
        }
    }
    
    public HashMap<String, Member> getMembers() {
        return members;
    }
    
    public ArrayList<String> getViceChairman() {
        return viceChairman;
    }
    
    public ArrayList<String> getManager() {
        return manager;
    }
    
    public boolean hasPlayer(String p) {
        return members.containsKey(p);
    }
    
    public boolean isAdvancedPlayer(String p) {
        Member member = getMember(p);
        return member.isAdvanced();
    }
    
    public boolean getResidenceFLag() {
        return residenceFLag;
    }
    
    public Member getMember(String p) {
        return members.get(p);
        
    }
    
    //此处留言板上限暂定为15
    public boolean addMsgToBoard(String msg) {
        if (msgBoard.size() < 15) {
            msgBoard.add(msg);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isMsgBoardEmpty() {
        return msgBoard.isEmpty();
    }
    
    public void clearMsgBoard() {
        msgBoard.clear();
    }
    
    //从留言板中获取字符串
    public String getMsgFromBoard() {
        StringBuilder msg =
                new StringBuilder(GuildManager.getPlugin().colorFormat("&a&l留言板："));
        for (String str :
                msgBoard) {
            msg.append(str);
        }
        return msg.toString();
    }
    
    public ArrayList<String> getMsgBoard() {
        return msgBoard;
    }
    
    public boolean isHasChangedName() {
        return hasChangedName;
    }
    
    public void setHasChangedName(boolean hasChangedName) {
        this.hasChangedName = hasChangedName;
    }
    
    //成员权限方法
    //调用Bukkit
    public void giveGuildSquarePerm(String player) {
        new ServerCmd().sendConsoleCmd("res pset main.gh " + player + " move true");
    }
    
    public void removeGuildSquarePerm(String player) {
        new ServerCmd().sendConsoleCmd("res pset main.gh " + player + " move remove");
    }
    
    public void removeResidence() {
        new ServerCmd().sendConsoleCmd("resadmin remove guild_" + id);
        new ServerCmd().sendConsoleCmd("resadmin confirm");
        residenceFLag = false;
    }
    
    //查看公会情况，均为返回字符串
    public String checkGuildName() {
        return "§2公会名: " + "§a" + guildName + "\n";
    }
    
    public String checkChairman() {
        return "§2会长: " + "§6" + chairMan + "\n";
    }
    
    public String checkCash() {
        return "§2公会资金: " + "§6" + cash + "\n";
    }
    
    public String checkPoints() {
        return "§2公会积分: " + "§6" + points + "\n";
    }
    
    public String checkMemSize() {
        return "§2公会人数: " + "§a" + members.size() + "/" + maxPlayers + "\n";
    }
    
    public String checkAdvancedMemSize() {
        return "§2高级玩家: " + "§a" + advancedPlayers + "/" + maxAdvancedPlayers + "\n";
    }
    
    public String checkStatus() {
        String msg = "§2" + "公会ID: " + "§a" + id + "\n";
        msg += checkGuildName();
        msg += checkChairman();
        msg += checkCash();
        msg += checkPoints();
        msg += checkMemSize();
        msg += checkAdvancedMemSize();
        msg += listMembers();
        return msg;
    }
    
    public String checkViceChairman() {
        StringBuilder str = new StringBuilder("§2副会长：");
        for (String s :
                viceChairman) {
            str.append(s).append(" ");
        }
        return str.toString();
    }
    
    public String checkManager() {
        StringBuilder str = new StringBuilder("§2管理员：");
        for (String s :
                manager) {
            str.append(s).append(" ");
        }
        return str.toString();
    }
    
    //输出成员列表
    public String listMembers() {
        StringBuilder msg = new StringBuilder("§2成员列表: ");
        if (members.size() == 0) {
            msg.append("[空]");
            return msg.toString();
        }
        for (String p : members.keySet()) {
            msg.append("§e").append(p).append(", ");
        }
        return msg.toString();
    }
    
    //输出高级成员列表
    public String listAdvancedMembers() {
        StringBuilder msg = new StringBuilder("§2高级成员列表: ");
        for (String p : members.keySet()) {
            if (getMember(p).isAdvanced()) {
                msg.append("§6").append(p).append(", ");
            }
        }
        return msg.toString();
    }
    
    //判断玩家是否为会长/副会长/管理员
    public Boolean hasLeader(String p) {
        return hasChairman(p) || hasManager(p);
    }
    
    public ArrayList<String> getApplicantList() {
        return applicantList;
    }
    
}

