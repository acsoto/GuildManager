package com.mcatk.guildmanager.sql;

public enum SQLCommand {
    CREATE_GUILD(
            "INSERT INTO `guild` " +
                    "(`guild_name`, `guild_chairman`)" +
                    "VALUES " +
                    "(?,?)"
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
