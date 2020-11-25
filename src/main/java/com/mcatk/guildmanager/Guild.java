package com.mcatk.guildmanager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;


public class Guild implements ConfigurationSerializable {
    private final GuildManager plugin = GuildManager.plugin;
    private final ConfigurationSection GuildsSec =
            plugin.getConfig().getConfigurationSection("Guilds");
    //需要保存的成员变量
    private final String ID; //标记ID
    private String GuildName; //公会名
    private String ChairMan; //会长
    private ArrayList<String> viceChairman; //副会长
    private ArrayList<String> Manager; //干事
    private int Level; //等级
    private int MaxPlayers; //最大成员数
    private int AdvancedPlayers; //高级成员数
    private int MaxAdvancedPlayers; //最大高级成员数
    private int Points; //积分
    private int RemoveMemLimitFlag; //成员删除限制flag
    private boolean ResidenceFLag; //是否拥有领地
    private HashMap<String , Member> Members= new HashMap<>(); //成员
    private int Cash; //资金

    //构造方法
    public Guild(String ID) {
        this.ID = ID;
        this.GuildName = ID;
        this.Level = 1;
        this.MaxPlayers = 10;
        this.MaxAdvancedPlayers = 5;
    }
    public Guild(String ID, String player) {
        this.ID = ID;
        this.GuildName = ID;
        setChairman(player);
        this.Level = 1;
        this.MaxPlayers = 10;
        this.MaxAdvancedPlayers = 5;
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
                (ArrayList<String>) map.get("viceChairman"):null;
        this.Manager=map.get("Manager") !=null?
                (ArrayList<String>) map.get("Manager"):null;
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
                (HashMap<String, Member>) map.get("Members"):null;
        this.Cash=map.get("Cash") !=null?
                (int)map.get("Cash"):0;
        saveConfig();
    }
    //成员变量的操作
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
    int getAdvancedPlayersNum(){
        return MaxAdvancedPlayers;
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
    Boolean removeAdvancedMembers(String p){
        Member member = Members.get(p);
        if(member!=null){
            removePerm(p);
            saveConfig();
            return true;
        }
        else return false;
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
    //成员权限方法
    void givePerm(String p){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"res pset main.gh "+p+" move true");
    }
    void removePerm(String p){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"res pset main.gh "+p+" move remove");
    }
    //领地操作
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
    //查看公会情况
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
    //存储方法
    void saveConfig(){
        GuildsSec.set(this.ID,this);
        plugin.saveConfig();
    }
}

