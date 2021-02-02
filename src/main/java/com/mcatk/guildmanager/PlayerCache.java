package com.mcatk.guildmanager;

import java.util.ArrayList;

public class PlayerCache {

    ArrayList<String> ApplicantList;

    public PlayerCache() {
        ApplicantList = new ArrayList<>();
    }

    public void addApplicant(String playerID){
        ApplicantList.add(playerID);
    }
    public boolean removeApplicant(String playerID){
        return ApplicantList.remove(playerID);
    }
}
