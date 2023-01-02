package com.mcatk.guildmanager.models;

public class Member {
    private String id;
    private String guildID;
    private int contribution;
    private boolean isAdvanced;

    public Member(String id, String guildID, int contribution, boolean isAdvanced) {
        this.id = id;
        this.guildID = guildID;
        this.contribution = contribution;
        this.isAdvanced = isAdvanced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public boolean isAdvanced() {
        return isAdvanced;
    }

    public void setAdvanced(boolean advanced) {
        isAdvanced = advanced;
    }
}
