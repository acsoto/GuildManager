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
    private final String ID;
    private String GuildName;
    private String ChairMan;
    private int Level;
    private int MaxPlayers;
    private int AdvancedPlayers;
    private int MaxAdvancedPlayers;
    private int Points;
    private int RemoveMemLimitFlag;
    private boolean ResidenceFLag;
    private HashMap<String , Member> Members= new HashMap<>();
    private int Cash;

    //只有ID的构造方法
    public Guild(String ID) {
        this.ID = ID;
        this.GuildName = ID;
        this.ChairMan = "";
        this.Level = 1;
        this.MaxPlayers = 5;
        this.Points = 0;
        this.RemoveMemLimitFlag=0;
        this.Cash=0;
    }
    //实现序列化
    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map=new HashMap<>();
        map.put("ID",ID);
        map.put("GuildName",GuildName);
        map.put("ChairMan",ChairMan);
        map.put("Level",Level);
        map.put("MaxPlayers",MaxPlayers);
        map.put("MaxAdvancedPlayers",MaxAdvancedPlayers);
        map.put("Points",Points);
        map.put("RemoveMemLimitFlag",RemoveMemLimitFlag);
        map.put("ResidenceFLag",ResidenceFLag);
        map.put("Members",Members);
        map.put("Cash",Cash);
        return map;
    }
    //反序列化
    public Guild(Map<String, Object> map){
        this.ID = (String)map.get("ID");
        this.GuildName=(String)map.get("GuildName");
        this.ChairMan=(String)map.get("ChairMan");
        this.Level=(int)map.get("Level");
        this.MaxPlayers=(int)map.get("MaxPlayers");
        this.MaxAdvancedPlayers=(int)map.get("MaxAdvancedPlayers");
        this.Points=(int)map.get("Points");
        this.RemoveMemLimitFlag=(int)map.get("RemoveMemLimitFlag");
        this.ResidenceFLag= (boolean) map.get("ResidenceFLag");
        this.Members=(HashMap<String, Member>) map.get("Members");
        this.Cash=(int) map.get("Cash");
    }
    //成员变量的操作
    String getID(){
        return ID;
    }
    void setName(String name){
        GuildName = name;
        saveConfig();
    }
    String getName(){
        return GuildName;
    }
    //会长操作
    void setChairman(String p){

        ChairMan=p;
        saveConfig();
    }
    String getChairman(){
        return ChairMan;
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
    String getStatus(){
        String msg="§2"+"公会ID: "+"§a"+ID+"\n";
        msg+="§2"+"公会名: "+"§a"+GuildName+"\n";
        msg+="§2"+"会长: "+"§6"+ChairMan+"\n";
        msg+="§2"+"公会资金: "+"§6"+Cash+"\n";
        msg+="§2"+"公会人数: "+"§a"+Members.size()+"/"+MaxPlayers+"\n";
        msg+="§2"+"高级玩家: "+"§a"+AdvancedPlayers+"/"+MaxAdvancedPlayers+"\n";
        msg+=listMembers();
        return msg;
    }
    //输出成员列表
    String listMembers(){
        StringBuilder msg= new StringBuilder("§2成员列表: \n");
        for(String p:Members.keySet()){
            msg.append("§e").append(p).append("\n");
        }
        return msg.toString();
    }
    //存储方法
    void saveConfig(){
        GuildsSec.set(this.ID,this);
        plugin.saveConfig();
    }
}

