package com.mcatk.guildmanager;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Member implements ConfigurationSerializable {
    private final String ID;
    private int contribution;
    private boolean isAdvanced;
    //构造方法
    Member(String ID){
        this.ID = ID;
        contribution = 0;
        isAdvanced = false;
    }
    //实现序列化
    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map=new HashMap<>();
        map.put("ID",ID);
        map.put("contribution",contribution);
        map.put("isAdvanced",isAdvanced);
        return map;
    }
    //反序列化
    public Member(Map<String, Object> map){
        this.ID = (String) map.get("ID");
        this.contribution=(int)map.get("contribution");
        this.isAdvanced=(boolean)map.get("isAdvanced");
    }
    //ID
    public String getID() {
        return ID;
    }
    //高级成员
    public boolean isAdvanced() {
        return isAdvanced;
    }
    public void setAdvanced(boolean b) {
        isAdvanced = b;
    }
    //贡献度
    public int getContribution() {
        return contribution;
    }
    public Boolean addContribution(int n) {
        if(contribution>=100)
            return false;
        else contribution += n;
        return true;
    }
    public void resetContribution() {
        contribution = 0;
    }

}
