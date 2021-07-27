package com.mcatk.guildmanager;

public enum Msg {
    INFO("§6§l公会系统 §7>>> §a"),
    ERROR("§4§l错误 §7>>> §c"),
    GUI_BACK("§7返回"),
    GUILD_GUI_TP("§5传送到公会"),
    
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
