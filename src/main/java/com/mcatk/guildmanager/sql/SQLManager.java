package com.mcatk.guildmanager.sql;

import com.mcatk.guildmanager.GuildManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {
    private Connection connection;

    private static SQLManager instance = null;

    public static SQLManager getInstance() {
        return instance == null ? instance = new SQLManager() : instance;
    }

    private SQLManager() {
        connectMySQL();
    }

    private void connectMySQL() {
        String ip = GuildManager.getPlugin().getConfig().getString("mysql.ip");
        String databaseName = GuildManager.getPlugin().getConfig().getString("mysql.databasename");
        String userName = GuildManager.getPlugin().getConfig().getString("mysql.username");
        String userPassword = GuildManager.getPlugin().getConfig().getString("mysql.password");
        int port = GuildManager.getPlugin().getConfig().getInt("mysql.port");
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?autoReconnect=true",
                    userName, userPassword
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createGuild(String name, String chairman) {
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.CREATE_GUILD.toString());
            ps.setString(1, name);
            ps.setString(2, chairman);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
