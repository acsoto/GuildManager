package com.mcatk.guildmanager;

import com.mcatk.guildmanager.file.FileOperation;

import java.util.HashMap;

public class Guilds {
    private HashMap<String, Guild> guildMap;
    
    public Guilds() {
        guildMap = new HashMap<>();
    }
    
    public void addGuild(String id, String player) {
        Guild g = new Guild(id, player);
        guildMap.put(id, g);
        FileOperation.saveGuilds();
    }
    
    public boolean removeGuild(String id) {
        if (guildMap.remove(id) != null) {
            FileOperation.saveGuilds();
            return true;
        }
        return false;
    }
    
    public HashMap<String, Guild> getGuildMap() {
        return guildMap;
    }
    
    //查公会
    public boolean hasGuild(String id) {
        return guildMap.containsKey(id);
    }
    
    public Guild getGuild(String id) {
        if (hasGuild(id)) {
            return guildMap.get(id);
        }
        return null;
    }
    
    public Guild getPlayersGuild(String p) {
        for (String key : guildMap.keySet()) {
            Guild g = guildMap.get(key);
            if (g.hasPlayer(p)) {
                return g;
            }
        }
        return null;
    }
    
    public boolean isPlayerInAnyApplicantList(String p) {
        for (String key :
                guildMap.keySet()) {
            Guild g = getGuild(key);
            if (g.getApplicantList().contains(p)) {
                return true;
            }
        }
        return false;
    }
}
