package com.mcatk.guildmanager.models;

import com.mcatk.guildmanager.sql.SQLManager;

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

    public boolean contains(String playerID) {
        for (ArrayList<String> list : listHashMap.values()) {
            if (list.contains(playerID)) {
                return true;
            }
        }
        return false;
    }

    public void add(String guildID, String playerID) {
        if (!listHashMap.containsKey(guildID)) {
            listHashMap.put(guildID, new ArrayList<>());
        }
        listHashMap.get(guildID).add(playerID);
    }


}
