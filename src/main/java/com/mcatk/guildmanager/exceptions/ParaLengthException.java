package com.mcatk.guildmanager.exceptions;

import com.mcatk.guildmanager.Msg;

public class ParaLengthException extends Exception {
    private final int length;
    
    public ParaLengthException(int length) {
        this.length = length;
    }
    
    @Override
    public String toString() {
        return Msg.ERROR + "§c参数长度有误，长度应为 " + length + "，请重试";
    }
}
