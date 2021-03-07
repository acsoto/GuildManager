package com.mcatk.guildmanager;

public class Member {
    private final String id;     //标记玩家ID
    private int contribution;    //贡献度
    private boolean isAdvanced;  //是否为高级成员
    
    //构造方法
    Member(String id) {
        this.id = id;
        contribution = 0;
        isAdvanced = false;
    }
    
    //ID
    public String getId() {
        return id;
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
        if (contribution >= 100) {
            return false;
        } else {
            contribution += n;
        }
        return true;
    }
    
}
