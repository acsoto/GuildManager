package com.mcatk.guildmanager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class Guild implements ConfigurationSerializable {
    private final GuildManager plugin = GuildManager.plugin;
    private final ConfigurationSection GuildsSec =
            plugin.getConfig().getConfigurationSection("Guilds");
    private final ArrayList<String> msgBoard = new ArrayList<>(); //留言板
    //需要保存的成员变量
    private final String ID; //标记ID
    private String GuildName; //公会名
    private String ChairMan; //会长
    private ArrayList<String> viceChairman = new ArrayList<>(); //副会长
    private ArrayList<String> Manager = new ArrayList<>(); //管理员
    private int Level; //等级
    private int MaxPlayers; //最大成员数
    private int AdvancedPlayers; //高级成员数
    private int MaxAdvancedPlayers; //最大高级成员数
    private int Points; //积分
    private int RemoveMemLimitFlag; //成员删除限制flag
    private boolean ResidenceFLag; //是否拥有领地
    private HashMap<String , Member> Members = new HashMap<>(); //成员
    private int Cash; //资金
    private String League; //联盟
    private String Ally; //伙伴公会
    private int ReposSize;
    private boolean HasChangedName;
    private ArrayList<String> ApplicantList =  new ArrayList<>();

    //构造方法
    public Guild(String ID, String player) {
        this.ID = ID;
        this.GuildName = ID;
        this.setChairman(player);
        this.Level = 1;
        this.MaxPlayers = 10;
        this.MaxAdvancedPlayers = 5;
        this.ReposSize = 9;
    }
    //实现序列化
    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map=new HashMap<>();
        map.put("ID",ID);
        map.put("GuildName",GuildName);
        map.put("ChairMan",ChairMan);
        map.put("viceChairman",viceChairman);
        map.put("Manager",Manager);
        map.put("Level",Level);
        map.put("MaxPlayers",MaxPlayers);
        map.put("AdvancedPlayers",AdvancedPlayers);
        map.put("MaxAdvancedPlayers",MaxAdvancedPlayers);
        map.put("Points",Points);
        map.put("RemoveMemLimitFlag",RemoveMemLimitFlag);
        map.put("ResidenceFLag",ResidenceFLag);
        map.put("Members",Members);
        map.put("Cash",Cash);
        map.put("League",League);
        map.put("Ally",Ally);
        map.put("ReposSize",ReposSize);
        map.put("HasChangedName",HasChangedName);
        return map;
    }
    //反序列化构造方法
    @SuppressWarnings("unchecked")
    public Guild(Map<String, Object> map){
        this.ID = map.get("ID") !=null?
                (String)map.get("ID"):null;
        this.GuildName=map.get("GuildName") !=null?
                (String)map.get("GuildName"):"";
        this.ChairMan=map.get("ChairMan") !=null?
                (String)map.get("ChairMan"):"";
        this.viceChairman=map.get("viceChairman") !=null?
                (ArrayList<String>) map.get("viceChairman"):new ArrayList<>();
        this.Manager=map.get("Manager") !=null?
                (ArrayList<String>) map.get("Manager"):new ArrayList<>();
        this.Level=map.get("Level") !=null?
                (int)map.get("Level"):1;
        this.MaxPlayers=map.get("MaxPlayers") !=null?
                (int)map.get("MaxPlayers"):10;
        this.AdvancedPlayers=map.get("AdvancedPlayers") !=null?
                (int)map.get("AdvancedPlayers"):0;
        this.MaxAdvancedPlayers=map.get("MaxAdvancedPlayers") !=null?
                (int)map.get("MaxAdvancedPlayers"):5;
        this.Points=map.get("Points") !=null?
                (int)map.get("Points"):0;
        this.RemoveMemLimitFlag=map.get("RemoveMemLimitFlag") !=null?
                (int)map.get("RemoveMemLimitFlag"):0;
        this.ResidenceFLag= map.get("ResidenceFLag") != null && (boolean) map.get("ResidenceFLag");
        this.Members=map.get("Members") !=null?
                (HashMap<String, Member>) map.get("Members"):new HashMap<>();
        this.Cash=map.get("Cash") !=null?
                (int)map.get("Cash"):0;
        this.League=map.get("League") !=null?
                (String) map.get("League"):"";
        this.Ally=map.get("Ally") !=null?
                (String) map.get("Ally"):"";
        this.ReposSize=map.get("ReposSize") !=null?
                (int) map.get("ReposSize"):9;
        this.HasChangedName= map.get("HasChangedName") != null && (boolean) map.get("HasChangedName");
        saveConfig();
    }
    //成员变量增删查改
    String getID(){
        return ID;
    }
    void setName(String name){
        GuildName = plugin.colorFormat(name);
        saveConfig();
    }
    String getName(){
        return GuildName;
    }
    void setChairman(String p){
        ChairMan=p;
        addMembers(p);
        saveConfig();
    }
    String getChairman(){
        return ChairMan;
    }
    Boolean setViceChairman(String p){
        if(viceChairman.size()>=2)
            return false;
        viceChairman.add(p);
        return true;
    }
    Boolean removeViceChairman(String p){
        return viceChairman.remove(p);
    }
    Boolean hasViceChairman(String p){
        return viceChairman.contains(p);
    }
    Boolean setManager(String p){
        if(Manager.size()>=3)
            return false;
        Manager.add(p);
        return true;
    }
    Boolean removeManager(String p){
        return Manager.remove(p);
    }
    Boolean hasManager(String p){
        return Manager.contains(p);
    }

    public int getReposSize() {
        return ReposSize;
    }

    public void setReposSize(int reposSize) {
        ReposSize = reposSize;
    }
    //仓库升级
    boolean levelUpReposSize(){
        if(ReposSize==54)
            return false;
        ReposSize+=9;
        return true;
    }

    //等级&最大玩家数&积分操作
    int levelUP(){
        int reqPoints=Level*5;
        int reqCash=Level*10+20;
        if(Points<reqPoints)
            return 1;
        if(Cash<reqCash)
            return 2;
        Cash-=reqCash;
        Level++;
        MaxPlayers+=5;
        MaxAdvancedPlayers+=2;
        return 3;
    }
    int getLevel() {
        return Level;
    }
    int getPlayersNum(){
        return Members.size();
    }
    int getMaxPlayers() {
        return MaxPlayers;
    }
    int getMaxAdvancedPlayers() {
        return MaxAdvancedPlayers;
    }
    int getPoints(){
        return Points;
    }
    void addCash(int n){
        Cash+=n;
        saveConfig();
    }
    Boolean takeCash(int n){
        if((Cash-n)<0)
            return false;
        Cash-=n;
        saveConfig();
        return true;
    }
    void addPoints(int n){
        Points+=n;
        saveConfig();
    }
    Boolean takePoints(int n){
        if((Points-n)<0)
            return false;
        Points-=n;
        saveConfig();
        return true;
    }
    int getCash(){
        return Cash;
    }
    //公会成员操作
    Boolean addMembers(String p){
        Member member = new Member(p);
        if(Members.size()<=MaxPlayers){
            Members.put(p , member);
            saveConfig();
            return true;
        }
        else return false;
    }
    //删除成员并且删除其权限和职位并保存
    Boolean removeMembers(String p){
        Member member = Members.remove(p);
        if(member!=null){
            removePerm(p);
            removeViceChairman(p);
            removeManager(p);
            saveConfig();
            return true;
        }
        else return false;
    }
    //设置玩家为高级成员并保存
    //判断是否满员
    int addAdvancedMembers(String p){
        Member member = Members.get(p);
        if (member!=null){
            if(member.isAdvanced()){
                return 0;
            }
            else if(AdvancedPlayers < MaxAdvancedPlayers){
                member.setAdvanced(true);
                AdvancedPlayers++;
                givePerm(p);
                saveConfig();
                return 1;
            }
            else return 2;
        }
        return 3;
    }
    //删除玩家的高级权限并保存
    Boolean removeAdvancedMembers(String p){
        Member member = Members.get(p);
        if(member!=null){
            removePerm(p);
            saveConfig();
            return true;
        }
        else return false;
    }

    HashMap<String,Member> getMembers(){
        return Members;
    }


    public ArrayList<String> getViceChairman() {
        return viceChairman;
    }

    public ArrayList<String> getManager() {
        return Manager;
    }


    Boolean hasPlayer(String p){
        return Members.containsKey(p);
    }

    Boolean isAdvancedPlayer(String p){
        Member member = getMember(p);
        return member.isAdvanced();
    }

    void addRemoveMemLimitFlag(){
        RemoveMemLimitFlag++;
    }

    void resetRemoveMemLimitFlag(){
        RemoveMemLimitFlag = 0;
    }

    int getRemoveMemLimitFlag(){
        return RemoveMemLimitFlag;
    }

    Boolean getResidenceFLag(){
        return ResidenceFLag;
    }

    Member getMember(String p){
        return Members.get(p);

    }
    //此处留言板上限暂定为15
    Boolean addMsgToBoard(String msg){
        if(msgBoard.size()<15){
            msgBoard.add(msg);
            return true;
        }
        else return false;
    }

    Boolean isMsgBoardEmpty(){
        return msgBoard.isEmpty();
    }

    void clearMsgBoard(){
        msgBoard.clear();
    }

    //从留言板中获取字符串
    String getMsgFromBoard(){
        StringBuilder msg =
                new StringBuilder(plugin.colorFormat("&a&l留言板："));
        for (String aMsg:
             msgBoard) {
            msg.append(aMsg);
        }
        return msg.toString();
    }

    public ArrayList<String> getMsgBoard() {
        return msgBoard;
    }

    public boolean isHasChangedName() {
        return HasChangedName;
    }

    public void setHasChangedName(boolean hasChangedName) {
        HasChangedName = hasChangedName;
    }

    //成员权限方法
    //调用Bukkit
    void givePerm(String p){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"res pset main.gh "+p+" move true");
    }
    void removePerm(String p){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"res pset main.gh "+p+" move remove");
    }
    //领地操作
    //调用Bukkit
    void createResidence(Player player){
        player.setOp(true);
        player.chat("/resadmin select vert");
        player.chat("/resadmin create guild_"+ID);
        player.setOp(false);
        ResidenceFLag=true;
    }
    void removeResidence(){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"resadmin remove guild_"+ID);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"resadmin confirm");
        ResidenceFLag=false;
    }
    //查看公会情况，均为返回字符串
    String checkGuildName(){
        return "§2公会名: "+"§a"+GuildName+"\n";
    }
    String checkChairman(){
        return "§2会长: "+"§6"+ChairMan+"\n";
    }
    String checkCash(){
        return "§2公会资金: "+"§6"+Cash+"\n";
    }
    String checkPoints(){
        return "§2公会积分: "+"§6"+Points+"\n";
    }
    String checkMemSize(){
        return "§2公会人数: "+"§a"+Members.size()+"/"+MaxPlayers+"\n";
    }
    String checkAdvancedMemSize(){
        return "§2高级玩家: "+"§a"+AdvancedPlayers+"/"+MaxAdvancedPlayers+"\n";
    }
    String checkStatus(){
        String msg="§2"+"公会ID: "+"§a"+ID+"\n";
        msg+= checkGuildName();
        msg+= checkChairman();
        msg+= checkCash();
        msg+= checkPoints();
        msg+= checkMemSize();
        msg+= checkAdvancedMemSize();
        msg+= listMembers();
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
                Manager) {
            str.append(s).append(" ");
        }
        return str.toString();
    }
    //输出成员列表
    String listMembers(){
        StringBuilder msg= new StringBuilder("§2成员列表: ");
        if(Members.size()==0){
            msg.append("[空]");
            return msg.toString();
        }
        for(String p:Members.keySet()){
            msg.append("§e").append(p).append(", ");
        }
        return msg.toString();
    }
    //输出高级成员列表
    String listAdvancedMembers(){
        StringBuilder msg= new StringBuilder("§2高级成员列表: ");
        for(String p:Members.keySet()){
            if(getMember(p).isAdvanced())
                msg.append("§6").append(p).append(", ");
        }
        return msg.toString();
    }

    void setAlly(String ally) {
        Ally = ally;
    }

    String getAlly() {
        return Ally;
    }

    void setLeague(String league) {
        League = league;
    }

    String getLeague() {
        return League;
    }

    //判断玩家是否为会长/副会长/管理员
    Boolean isLeader(String p){
        if(ChairMan.equals(p))
            return true;
        if (hasManager(p))
            return true;
        return hasViceChairman(p);
    }

    public ArrayList<String> getApplicantList() {
        return ApplicantList;
    }

    //存储方法
    void saveConfig(){
        GuildsSec.set(this.ID,this);
        plugin.saveConfig();
    }
}

