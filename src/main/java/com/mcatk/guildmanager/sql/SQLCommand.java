package com.mcatk.guildmanager.sql;

public enum SQLCommand {
    CREATE_GUILD(
            "INSERT INTO `guild` " +
                    "(`guild_name`, `guild_chairman`)" +
                    "VALUES " +
                    "(?,?)"
    ),
    CREATE_GUILD_FULL_PARAS(
            "INSERT INTO player (guild_id, guild_name, guild_chairman, guild_level, guild_points, guild_cash, " +
                    "guild_has_residence, guild_has_changed_name) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?)"
    ),
    CREATE_PLAYER_FULL_PARAS(
            "INSERT INTO player (player_id, guild_id, player_is_advanced, player_contribution) " +
                    "VALUES " +
                    "(?, ?, ?, ?)"
    ),
    UPDATE_GUILD(
            "UPDATE guild t " +
                    "SET t.guild_name             = ?, " +
                    "    t.guild_chairman         = ?, " +
                    "    t.guild_level            = ?, " +
                    "    t.guild_points           = ?, " +
                    "    t.guild_cash             = ?, " +
                    "    t.guild_has_residence    = ?, " +
                    "    t.guild_has_changed_name = ? " +
                    "WHERE t.guild_id = ?"
    ),
    GET_ALL_GUILDS(
            "SELECT * FROM guild"
    ),
    GET_PLAYER_GUILD(
            "SELECT * FROM player JOIN guild g on player.guild_id = g.guild_id WHERE player_id = ?"
    );

    private final String command;

    SQLCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }
}
