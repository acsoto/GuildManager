package com.mcatk.guildmanager;

import org.bukkit.Bukkit;

public class ServerCmd {
    
    
    
    public static void sendConsoleCmd(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
    
    public static void addPermission(String player, String permission) {
        sendConsoleCmd("lp user " + player + " permission set " + permission + " true");
    }
    
    public static void removePermission(String player, String permission) {
        sendConsoleCmd("lp user " + player + " permission clear " + permission);
    }
    
    
}
