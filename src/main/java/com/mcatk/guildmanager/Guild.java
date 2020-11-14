package com.mcatk.guildmanager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;


public class Guild implements ConfigurationSerializable {
    //成员变量
    private final String ID;
    private String GuildName;
    private String ChairMan;
    private int Level;
    private int MaxPlayers;
    private int MaxAdvancedPlayers;
    private int Points;
    private int RemoveMemLimitFlag;
    private boolean ResidenceFLag;
    private ArrayList<String> Members= new ArrayList<>();
    private ArrayList<String> AdvancedMembers=new ArrayList<>();
    private long Cash;

    ConfigurationSection config = GuildManager.plugin.getConfig().getConfigurationSection("Guilds");
    //构造方法
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
    //成员变量set和get
    String getID(){
        return ID;
    }
    void setName(String name){
        GuildName = name;
        config.set(this.ID,this);
    }
    String getName(){
        return GuildName;
    }
    //会长操作
    void setChairman(String p){
        ChairMan=p;
        config.set(this.ID,this);
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
    long getCash(){
        return Cash;
    }
    //公会成员操作
    Boolean addMembers(String p){
        if(Members.size()<=MaxPlayers){
            Members.add(p);
            saveConfig();
            return true;
        }
        else return false;
    }
    Boolean removeMembers(String p){
        if(Members.remove(p)){
            saveConfig();
            return true;
        }
        else return false;
    }
    Boolean addAdvancedMembers(String p){
        if (Members.contains(p)){
            if(AdvancedMembers.size()<=MaxAdvancedPlayers){
                AdvancedMembers.add(p);
                givePerm(p);
                saveConfig();
                return true;
            }
        }
        return false;
    }
    Boolean removeAdvancedMembers(String p){
        if(AdvancedMembers.remove(p)){
            removePerm(p);
            saveConfig();
            return true;
        }
        else return false;
    }
    ArrayList<String> getMembers() {
        return Members;
    }
    ArrayList<String> getAdvancedMembers() {
        return AdvancedMembers;
    }
    Boolean hasPlayer(String p){
        return Members.contains(p);
    }
    Boolean hasAdvancedPlayer(String p){
        return AdvancedMembers.contains(p);
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
        msg+="§2"+"高级玩家: "+"§a"+AdvancedMembers.size()+"/"+MaxAdvancedPlayers+"\n";
        msg+=listMembers();
        return msg;
    }
    //输出成员列表
    String listMembers(){
        StringBuilder msg= new StringBuilder("§2成员列表: \n");
        for(String p:Members){
            msg.append("§e").append(p).append("\n");
        }
        return msg.toString();
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
        map.put("Members",Members);
        map.put("AdvancedMembers",AdvancedMembers);
        map.put("RemoveMemLimitFlag",RemoveMemLimitFlag);
        map.put("ResidenceFLag",ResidenceFLag);
        map.put("Cash",Cash);
        return map;
    }

    public static Guild deserialize(Map<String, Object> map){
        Guild g = new Guild(
                (map.get("ID")!=null?(String)map.get("ID"):null)
        );
        g.GuildName=(map.get("GuildName")!=null?(String)map.get("GuildName"):"");
        g.ChairMan=(map.get("ChairMan")!=null?(String)map.get("ChairMan"):"");
        g.Level=(map.get("Level")!=null?(int)map.get("Level"):1);
        g.MaxPlayers=(map.get("MaxAdvancedPlayers")!=null?(int)map.get("MaxPlayers"):10);
        g.MaxAdvancedPlayers=(map.get("MaxAdvancedPlayers")!=null?(int)map.get("MaxPlayers"):5);
        g.Points=(map.get("Points")!=null?(int)map.get("Points"):0);
        g.RemoveMemLimitFlag=(map.get("RemoveMemLimitFlag")!=null?(int)map.get("RemoveMemLimitFlag"):0);
        g.ResidenceFLag=(map.get("ResidenceFLag") != null && (boolean) map.get("ResidenceFLag"));
        g.Members=(map.get("Members")!=null?(ArrayList<String>) map.get("Members"):new ArrayList<String>());
        g.AdvancedMembers=(map.get("AdvancedMembers")!=null?(ArrayList<String>) map.get("Members"):new ArrayList<String>());
        g.Cash=(map.get("Members")!=null?(long) map.get("Members"):0);
        return g;
    }

    void saveConfig(){
        config.set(this.ID,this);
        GuildManager.plugin.saveConfig();
    }
}

