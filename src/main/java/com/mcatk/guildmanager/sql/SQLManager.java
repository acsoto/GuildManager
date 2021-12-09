package com.mcatk.guildmanager.sql;

import com.mcatk.guildmanager.Guild;
import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.Guilds;
import com.mcatk.guildmanager.Member;

import java.sql.*;

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

    public String getPlayerGuild(String name) {
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_PLAYER_GUILD.toString());
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.getString("g.guild_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveGuilds() {
        try {
            Guilds guilds = GuildManager.getPlugin().getGuilds();
            for (Guild g : guilds.getGuildMap().values()) {
                PreparedStatement ps = connection.prepareStatement(SQLCommand.UPDATE_GUILD.toString());
                ps.setString(1, g.getName());
                ps.setString(2, g.getChairman());
                ps.setInt(3, g.getLevel());
                ps.setInt(4, g.getPoints());
                ps.setInt(5, g.getCash());
                ps.setInt(6, g.getResidenceFLag() ? 1 : 0);
                ps.setInt(7, g.isHasChangedName() ? 1 : 0);
                ps.setString(8, g.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Guilds loadGuilds() {
        Guilds guilds = new Guilds();
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_ALL_GUILDS.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Guild g = new Guild(
                        rs.getString("guild_id"),
                        rs.getString("guild_name"),
                        rs.getString("guild_chairman"),
                        rs.getInt("guild_level"),
                        rs.getInt("guild_points"),
                        rs.getBoolean("guild_has_residence"),
                        rs.getInt("guild_cash"),
                        rs.getBoolean("guild_has_changed_name")
                );
                guilds.getGuildMap().put(g.getId(), g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guilds;
    }


}
