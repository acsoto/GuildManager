package com.mcatk.guildmanager.sql;

import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.Member;

import java.sql.*;
import java.util.ArrayList;

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

    public Guild getPlayerGuild(String name) {
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_PLAYER_GUILD.toString());
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String guild_id = rs.getString("g.guild_id");
                if (guild_id == null) {
                    return null;
                }
                return getGuild(guild_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveGuild(Guild g) {
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.UPDATE_GUILD.toString());
            ps.setString(1, g.getGuildName());
            ps.setString(2, g.getChairman());
            ps.setInt(3, g.getLevel());
            ps.setInt(4, g.getPoints());
            ps.setInt(5, g.getCash());
            ps.setBoolean(6, g.getResidenceFLag());
            ps.setBoolean(7, g.getHasChangedName());
            ps.setString(8, g.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Guild getGuild(String id) {
        Guild g = null;
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_GUILD.toString());
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                g = new Guild();
                g.setId(rs.getString("guild_id"));
                g.setGuildName(rs.getString("guild_name"));
                g.setChairman(rs.getString("guild_chairman"));
                g.setLevel(rs.getInt("guild_level"));
                g.setPoints(rs.getInt("guild_points"));
                g.setCash(rs.getInt("guild_cash"));
                g.setResidenceFLag(rs.getBoolean("guild_has_residence"));
                g.setHasChangedName(rs.getBoolean("guild_has_changed_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return g;
    }

    public void saveMember(Member m) {
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.UPDATE_PLAYER.toString());
            ps.setBoolean(1, m.isAdvanced());
            ps.setInt(2, m.getContribution());
            ps.setString(3, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Member getMember(String id) {
        Member m = null;
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_PLAYER.toString());
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m = new Member();
                m.setId(rs.getString("player_id"));
                m.setGuildID(rs.getString("guild_id"));
                m.setAdvanced(rs.getBoolean("player_is_advanced"));
                m.setContribution(rs.getInt("player_contribution"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public void addMember(String playerID, String guildID) {
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.CREATE_PLAYER.toString());
            ps.setString(1, playerID);
            ps.setString(2, guildID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMember(String playerID) {
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.DELETE_PLAYER.toString());
            ps.setString(1, playerID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getGuildMembers(String id) {
        ArrayList<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_GUILD_PLAYERS.toString());
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("player_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getGuildAdvancedMembers(String id) {
        ArrayList<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_GUILD_ADVANCED_PLAYERS.toString());
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("player_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Guild> getAllGuilds() {
        ArrayList<Guild> list = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_ALL_GUILDS.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Guild g = new Guild();
                g.setId(rs.getString("guild_id"));
                g.setGuildName(rs.getString("guild_name"));
                g.setChairman(rs.getString("guild_chairman"));
                g.setLevel(rs.getInt("guild_level"));
                g.setPoints(rs.getInt("guild_points"));
                g.setCash(rs.getInt("guild_cash"));
                g.setResidenceFLag(rs.getBoolean("guild_has_residence"));
                g.setHasChangedName(rs.getBoolean("guild_has_changed_name"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
