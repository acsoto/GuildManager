package com.mcatk.guildmanager.models;

import java.util.Arrays;
import java.util.List;

public class GuildBasicInfo {
    private static final List<Integer> maxPlayerArray = Arrays.asList(10, 15, 20, 25, 30);
    private static final List<Integer> maxAdvancedPlayerArray = Arrays.asList(5, 7, 9, 11, 13);


    public static int getMaxPlayer(int level) {
        return maxPlayerArray.get(level);
    }

    public static int getMaxAdvancedPlayer(int level) {
        return maxAdvancedPlayerArray.get(level);
    }

}
