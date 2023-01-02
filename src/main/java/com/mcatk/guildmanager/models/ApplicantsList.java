package com.mcatk.guildmanager.models;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicantsList {
    private static ApplicantsList instance = null;

    public static ApplicantsList getApplicantsList() {
        return instance == null ? instance = new ApplicantsList() : instance;
    }

    private HashMap<String, ArrayList<String>> listHashMap = new HashMap<>();

    public ArrayList<String> getList(String guildID) {
        if (!listHashMap.containsKey(guildID)) {
            listHashMap.put(guildID, new ArrayList<>());
        }
        return listHashMap.get(guildID);
    }

    public void add(String guildID, String playerID) {
        if (!listHashMap.containsKey(guildID)) {
            listHashMap.put(guildID, new ArrayList<>());
        }
        listHashMap.get(guildID).add(playerID);
    }


}
