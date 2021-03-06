package com.mcatk.guildmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class Guild implements ConfigurationSerializable {
    private GuildManager plugin;
    private ArrayList<String> msgBoard; //留言板
    //需要保存的成员变量
    private final String id; //标记ID
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
        this.plugin = GuildManager.getPlugin();
        this.msgBoard = new ArrayList<>();
        this.viceChairman = new ArrayList<>();
        this.manager = new ArrayList<>();
        this.members = new HashMap<>();
        this.applicantList = new ArrayList<>();
    }
    
    //实现序列化
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", id);
        map.put("GuildName", guildName);
        map.put("ChairMan", chairMan);
        map.put("viceChairman", viceChairman);
        map.put("Manager", manager);
        map.put("Level", level);
        map.put("MaxPlayers", maxPlayers);
        map.put("AdvancedPlayers", advancedPlayers);
        map.put("MaxAdvancedPlayers", maxAdvancedPlayers);
        map.put("Points", points);
        map.put("RemoveMemLimitFlag", removeMemLimitFlag);
        map.put("ResidenceFLag", residenceFLag);
        map.put("Members", members);
        map.put("Cash", cash);
        map.put("ReposSize", reposSize);
        map.put("HasChangedName", hasChangedName);
        return map;
    }
    
    //反序列化构造方法
    @SuppressWarnings("unchecked")
    public Guild(Map<String, Object> map) {
        this.id = map.get("ID") != null ?
                (String) map.get("ID") : null;
        this.guildName = map.get("GuildName") != null ?
                (String) map.get("GuildName") : "";
        this.chairMan = map.get("ChairMan") != null ?
                (String) map.get("ChairMan") : "";
        this.viceChairman = map.get("viceChairman") != null ?
                (ArrayList<String>) map.get("viceChairman") : new ArrayList<>();
        this.manager = map.get("Manager") != null ?
                (ArrayList<String>) map.get("Manager") : new ArrayList<>();
        this.level = map.get("Level") != null ?
                (int) map.get("Level") : 1;
        this.maxPlayers = map.get("MaxPlayers") != null ?
                (int) map.get("MaxPlayers") : 10;
        this.advancedPlayers = map.get("AdvancedPlayers") != null ?
                (int) map.get("AdvancedPlayers") : 0;
        this.maxAdvancedPlayers = map.get("MaxAdvancedPlayers") != null ?
                (int) map.get("MaxAdvancedPlayers") : 5;
        this.points = map.get("Points") != null ?
                (int) map.get("Points") : 0;
        this.removeMemLimitFlag = map.get("RemoveMemLimitFlag") != null ?
                (int) map.get("RemoveMemLimitFlag") : 0;
        this.residenceFLag = map.get("ResidenceFLag") != null && (boolean) map.get("ResidenceFLag");
        this.members = map.get("Members") != null ?
                (HashMap<String, Member>) map.get("Members") : new HashMap<>();
        this.cash = map.get("Cash") != null ?
                (int) map.get("Cash") : 0;
        this.reposSize = map.get("ReposSize") != null ?
                (int) map.get("ReposSize") : 9;
        this.hasChangedName = map.get("HasChangedName") != null
                && (boolean) map.get("HasChangedName");
    }
    
    //成员变量增删查改
    String getId() {
        return id;
    }
    
    void setName(String name) {
        guildName = plugin.colorFormat(name);
    }
    
    String getName() {
        return guildName;
    }
    
    void setChairman(String p) {
        chairMan = p;
        addMembers(p);
    }
    
    String getChairman() {
        return chairMan;
    }
    
    Boolean setViceChairman(String p) {
        if (viceChairman.size() >= 2) {
            return false;
        }
        viceChairman.add(p);
        return true;
    }
    
    Boolean removeViceChairman(String p) {
        return viceChairman.remove(p);
    }
    
    Boolean hasViceChairman(String p) {
        return viceChairman.contains(p);
    }
    
    Boolean setManager(String p) {
        if (manager.size() >= 3) {
            return false;
        }
        manager.add(p);
        return true;
    }
    
    Boolean removeManager(String p) {
        return manager.remove(p);
    }
    
    public boolean hasManager(String p) {
        return manager.contains(p);
    }
    
    public boolean hasChairman(String p) {
        return p.equals(chairMan) || viceChairman.contains(p);
    }
    
    public int getReposSize() {
        return reposSize;
    }
    
    public void setReposSize(int reposSize) {
        this.reposSize = reposSize;
    }
    
    //仓库升级
    boolean levelUpReposSize() {
        if (reposSize == 54) {
            return false;
        }
        reposSize += 9;
        return true;
    }
    
    //等级&最大玩家数&积分操作
    int levelUP() {
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
    
    int getLevel() {
        return level;
    }
    
    int getPlayersNum() {
        return members.size();
    }
    
    int getMaxPlayers() {
        return maxPlayers;
    }
    
    int getMaxAdvancedPlayers() {
        return maxAdvancedPlayers;
    }
    
    int getPoints() {
        return points;
    }
    
    void addCash(int n) {
        cash += n;
    }
    
    Boolean takeCash(int n) {
        if ((cash - n) < 0) {
            return false;
        }
        cash -= n;
        return true;
    }
    
    void addPoints(int n) {
        points += n;
    }
    
    Boolean takePoints(int n) {
        if ((points - n) < 0) {
            return false;
        }
        points -= n;
        return true;
    }
    
    int getCash() {
        return cash;
    }
    
    //公会成员操作
    Boolean addMembers(String p) {
        Member member = new Member(p);
        if (members.size() <= maxPlayers) {
            members.put(p, member);
            return true;
        } else {
            return false;
        }
    }
    
    //删除成员并且删除其权限和职位并保存
    Boolean removeMembers(String p) {
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
    int addAdvancedMembers(String p) {
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
    Boolean removeAdvancedMembers(String p) {
        Member member = members.get(p);
        if (member != null) {
            removeGuildSquarePerm(p);
            return true;
        } else {
            return false;
        }
    }
    
    HashMap<String, Member> getMembers() {
        return members;
    }
    
    public ArrayList<String> getViceChairman() {
        return viceChairman;
    }
    
    public ArrayList<String> getManager() {
        return manager;
    }
    
    Boolean hasPlayer(String p) {
        return members.containsKey(p);
    }
    
    Boolean isAdvancedPlayer(String p) {
        Member member = getMember(p);
        return member.isAdvanced();
    }
    
    void addRemoveMemLimitFlag() {
        removeMemLimitFlag++;
    }
    
    void resetRemoveMemLimitFlag() {
        removeMemLimitFlag = 0;
    }
    
    int getRemoveMemLimitFlag() {
        return removeMemLimitFlag;
    }
    
    Boolean getResidenceFLag() {
        return residenceFLag;
    }
    
    Member getMember(String p) {
        return members.get(p);
        
    }
    
    //此处留言板上限暂定为15
    Boolean addMsgToBoard(String msg) {
        if (msgBoard.size() < 15) {
            msgBoard.add(msg);
            return true;
        } else {
            return false;
        }
    }
    
    Boolean isMsgBoardEmpty() {
        return msgBoard.isEmpty();
    }
    
    void clearMsgBoard() {
        msgBoard.clear();
    }
    
    //从留言板中获取字符串
    String getMsgFromBoard() {
        StringBuilder msg =
                new StringBuilder(plugin.colorFormat("&a&l留言板："));
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
    void giveGuildSquarePerm(String player) {
        ServerCmd.sendConsoleCmd("res pset main.gh " + player + " move true");
    }
    
    void removeGuildSquarePerm(String player) {
        ServerCmd.sendConsoleCmd("res pset main.gh " + player + " move remove");
    }
    
    void giveForgePerm(String player) {
        ServerCmd.addPermission(player, "epiccraftingsplus.categories.gonghui");
    }
    
    void removeForgePerm(String player) {
        ServerCmd.removePermission(player, "epiccraftingsplus.categories.gonghui");
    }
    
    //领地操作
    //调用Bukkit
    void createResidence(Player player) {
        player.setOp(true);
        player.chat("/resadmin select vert");
        player.chat("/resadmin create guild_" + id);
        player.setOp(false);
        residenceFLag = true;
    }
    
    void removeResidence() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "resadmin remove guild_" + id);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "resadmin confirm");
        residenceFLag = false;
    }
    
    //查看公会情况，均为返回字符串
    String checkGuildName() {
        return "§2公会名: " + "§a" + guildName + "\n";
    }
    
    String checkChairman() {
        return "§2会长: " + "§6" + chairMan + "\n";
    }
    
    String checkCash() {
        return "§2公会资金: " + "§6" + cash + "\n";
    }
    
    String checkPoints() {
        return "§2公会积分: " + "§6" + points + "\n";
    }
    
    String checkMemSize() {
        return "§2公会人数: " + "§a" + members.size() + "/" + maxPlayers + "\n";
    }
    
    String checkAdvancedMemSize() {
        return "§2高级玩家: " + "§a" + advancedPlayers + "/" + maxAdvancedPlayers + "\n";
    }
    
    String checkStatus() {
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
    
    String checkViceChairman() {
        StringBuilder str = new StringBuilder("§2副会长：");
        for (String s :
                viceChairman) {
            str.append(s).append(" ");
        }
        return str.toString();
    }
    
    String checkManager() {
        StringBuilder str = new StringBuilder("§2管理员：");
        for (String s :
                manager) {
            str.append(s).append(" ");
        }
        return str.toString();
    }
    
    //输出成员列表
    String listMembers() {
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
    String listAdvancedMembers() {
        StringBuilder msg = new StringBuilder("§2高级成员列表: ");
        for (String p : members.keySet()) {
            if (getMember(p).isAdvanced()) {
                msg.append("§6").append(p).append(", ");
            }
        }
        return msg.toString();
    }
    
    //判断玩家是否为会长/副会长/管理员
    Boolean hasLeader(String p) {
        return hasChairman(p) || hasManager(p);
    }
    
    public ArrayList<String> getApplicantList() {
        return applicantList;
    }
    
}

