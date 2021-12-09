//package com.mcatk.guildmanager.file;
//
//import com.google.gson.Gson;
//import com.mcatk.guildmanager.GuildManager;
//import com.mcatk.guildmanager.Guilds;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class FileOperation {
//    public void saveGuilds() {
//        Gson gson = new Gson();
//        try {
//            File file = new File(GuildManager.getPlugin().getDataFolder(), "guilds.json");
//            FileWriter writer = new FileWriter(file);
//            gson.toJson(GuildManager.getPlugin().getGuilds(), writer);
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Guilds loadGuilds() {
//        Gson gson = new Gson();
//        Guilds guilds = new Guilds();
//        try {
//            File file = new File(GuildManager.getPlugin().getDataFolder(), "guilds.json");
//            if (file.exists()) {
//                FileReader reader = new FileReader(file);
//                guilds = gson.fromJson(reader, Guilds.class);
//                if (guilds == null) {
//                    guilds = new Guilds();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return guilds;
//    }
//}
