package com.mcatk.guildmanager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.events.Event;


public class Guild implements ConfigurationSerializable {
    private GuildManager plugin = GuildManager.plugin;
    private ConfigurationSection GuildsSec =
            plugin.getConfig().getConfigurationSection("Guilds");
    //需要保存的成员变量
    private String ID;
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
        map.put("AdvancedMembers",AdvancedMembers);
        map.put("Cash",Cash);
        return map;
    }
    //反序列化
    public static Guild deserialize(Map<String, Object> map){
        Guild guild = new Guild((String)map.get("ID"));
        guild.GuildName=(String)map.get("GuildName");
        guild.ChairMan=(String)map.get("ChairMan");
        guild.Level=(int)map.get("Level");
        guild.MaxPlayers=(int)map.get("MaxPlayers");
        guild.MaxAdvancedPlayers=(int)map.get("MaxAdvancedPlayers");
        guild.Points=(int)map.get("Points");
        guild.RemoveMemLimitFlag=(int)map.get("RemoveMemLimitFlag");
        guild.ResidenceFLag= (boolean) map.get("ResidenceFLag");
        guild.Members=(ArrayList<String>) map.get("Members");
        guild.AdvancedMembers=(ArrayList<String>) map.get("AdvancedMembers");
        guild.Cash=(int) map.get("Cash");
        return guild;
    }
    //成员变量的操作
    String getID(){
        return ID;
    }
    void setName(String name){
        GuildName = name;
        GuildManager.plugin.getConfig().getConfigurationSection("Guilds").set(this.ID,this);
    }
    String getName(){
        return GuildName;
    }
    //会长操作
    void setChairman(String p){
        ChairMan=p;
        GuildManager.plugin.getConfig().getConfigurationSection("Guilds").set(this.ID,this);
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
    //存储方法
    void saveConfig(){
        GuildManager.plugin.getConfig().getConfigurationSection("Guilds").set(this.ID,this);
        GuildManager.plugin.saveConfig();
    }
}

