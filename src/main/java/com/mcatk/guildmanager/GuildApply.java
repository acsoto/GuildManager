package com.mcatk.guildmanager;

import java.util.ArrayList;

public class GuildApply {
    
    private ArrayList<String> applicantList;
    
    public GuildApply() {
        applicantList = new ArrayList<>();
    }
    
    public void addApplicant(String playerID) {
        applicantList.add(playerID);
    }
    
    public boolean removeApplicant(String playerID) {
        return applicantList.remove(playerID);
    }
}
