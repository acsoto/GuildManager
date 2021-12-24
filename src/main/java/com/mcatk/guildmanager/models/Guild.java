package com.mcatk.guildmanager.models;

import java.util.ArrayList;

public class Guild {
    private String id = "";
    private String guildName = "";
    private String chairman = "";
    private String viceChairman1 = "";
    private String viceChairman2 = "";
    private int level = 0;
    private int points = 0;
    private int cash = 0;
    private boolean residenceFLag = false;
    private boolean hasChangedName = false;
    private ArrayList<String> applicantList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getChairman() {
        return chairman;
    }

    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    public String getViceChairman1() {
        return viceChairman1;
    }

    public void setViceChairman1(String viceChairman1) {
        this.viceChairman1 = viceChairman1;
    }

    public String getViceChairman2() {
        return viceChairman2;
    }

    public void setViceChairman2(String viceChairman2) {
        this.viceChairman2 = viceChairman2;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public boolean getResidenceFLag() {
        return residenceFLag;
    }

    public void setResidenceFLag(boolean residenceFLag) {
        this.residenceFLag = residenceFLag;
    }

    public boolean getHasChangedName() {
        return hasChangedName;
    }

    public void setHasChangedName(boolean hasChangedName) {
        this.hasChangedName = hasChangedName;
    }

    public ArrayList<String> getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(ArrayList<String> applicantList) {
        this.applicantList = applicantList;
    }

    public boolean isManager(String playerID) {
        return chairman.equals(playerID) ||
                viceChairman1.equals(playerID) ||
                viceChairman2.equals(playerID);
    }
}