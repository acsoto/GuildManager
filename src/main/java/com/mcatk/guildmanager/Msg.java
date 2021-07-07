package com.mcatk.guildmanager;

public enum Msg {
    INFO("§6§l公会系统 §7>>> §a"),
    ERROR("§4§l错误 §7>>> §c"),
    
    ;
    private final String content;
    
    Msg(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return content;
    }
}
