package com.mcatk.guildmanager.sql;

public enum SQLCommand {
    CREATE_GUILD(
            "INSERT INTO `guild` " +
                    "(`guild_id`, `guild_chairman`)" +
                    "VALUES " +
                    "(?,?)"
    ),
    CREATE_PLAYER(
            "INSERT INTO `player_guild` " +
                    "(`player_id`, `guild_id`)" +
                    "VALUES " +
                    "(?,?)"
    ),
    DELETE_PLAYER(
            "DELETE FROM `player_guild` WHERE `player_id` = ?"
    ),
    UPDATE_GUILD(
            "UPDATE guild t " +
                    "SET t.guild_name             = ?, " +
                    "    t.guild_chairman         = ?, " +
                    "    t.guild_vice_chairman_1         = ?, " +
                    "    t.guild_vice_chairman_2         = ?, " +
                    "    t.guild_level            = ?, " +
                    "    t.guild_points           = ?, " +
                    "    t.guild_cash             = ?, " +
                    "    t.guild_has_residence    = ?, " +
                    "    t.guild_has_changed_name = ? " +
                    "WHERE t.guild_id = ?"
    ),
    GET_GUILD(
            "SELECT * FROM guild WHERE guild_id = ?"
    ),
    UPDATE_PLAYER(
            "UPDATE `player_guild` t " +
                    "SET t.player_is_advanced  = ?, " +
                    "    t.player_contribution = ? " +
                    "WHERE t.player_id = ?"
    ),
    GET_PLAYER(
            "SELECT * FROM player_guild WHERE player_id = ?"
    ),
    GET_ALL_GUILDS(
            "SELECT * FROM guild"
    ),
    GET_PLAYER_GUILD(
            "SELECT * FROM player_guild JOIN guild g on player_guild.guild_id = g.guild_id WHERE player_id = ?"
    ),
    GET_GUILD_PLAYERS(
            "SELECT * FROM player_guild WHERE guild_id = ?"
    ),
    GET_GUILD_ADVANCED_PLAYERS(
            "SELECT * FROM player_guild WHERE guild_id = ? AND player_is_advanced = true"
    ),




    ;

    private final String command;

    SQLCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }
}
