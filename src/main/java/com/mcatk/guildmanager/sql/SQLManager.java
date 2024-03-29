package com.mcatk.guildmanager.sql;

import com.mcatk.guildmanager.GuildManager;
import com.mcatk.guildmanager.models.Guild;
import com.mcatk.guildmanager.models.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLManager {
    private Connection connection;
    private HashMap<String, Guild> guilds;

    private static SQLManager instance = null;

    public static SQLManager getInstance() {
        return instance == null ? instance = new SQLManager() : instance;
    }

    private SQLManager() {
        connectMySQL();
        guilds = getAllGuildsFromSQL();
    }

    private void connectMySQL() {
        String ip = GuildManager.getPlugin().getConfig().getString("mysql.ip");
        String databaseName = GuildManager.getPlugin().getConfig().getString("mysql.databasename");
        String userName = GuildManager.getPlugin().getConfig().getString("mysql.username");
        String userPassword = GuildManager.getPlugin().getConfig().getString("mysql.password");
        int port = GuildManager.getPlugin().getConfig().getInt("mysql.port");
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?autoReconnect=true&useSSL=false",
                    userName, userPassword
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createGuild(String id, String chairman) {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.CREATE_GUILD.toString())){
            ps.setString(1, id);
            ps.setString(2, chairman);
            ps.executeUpdate();
            update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Guild getPlayerGuild(String name) {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_PLAYER_GUILD.toString())){
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
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.UPDATE_GUILD.toString())){
            ps.setString(1, g.getGuildName());
            ps.setString(2, g.getChairman());
            ps.setString(3, g.getViceChairman1());
            ps.setString(4, g.getViceChairman2());
            ps.setInt(5, g.getLevel());
            ps.setInt(6, g.getPoints());
            ps.setInt(7, g.getCash());
            ps.setBoolean(8, g.getResidenceFLag());
            ps.setBoolean(9, g.getHasChangedName());
            ps.setString(10, g.getId());
            ps.executeUpdate();
            update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Guild getGuild(String guildID) {
        return guilds.get(guildID);
    }

    public void saveMember(Member m) {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.UPDATE_PLAYER.toString())){
            ps.setBoolean(1, m.isAdvanced());
            ps.setInt(2, m.getContribution());
            ps.setString(3, m.getId());
            ps.executeUpdate();
            updateMembers(m.getGuildID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Member getMember(String id) {
        Member m = null;
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_PLAYER.toString())){
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m = new Member(
                        rs.getString("player_id"),
                        rs.getString("guild_id"),
                        rs.getInt("player_contribution"),
                        rs.getBoolean("player_is_advanced")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public void addMember(String playerID, String guildID) {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.CREATE_PLAYER.toString())) {
            ps.setString(1, playerID);
            ps.setString(2, guildID);
            ps.executeUpdate();
            updateMembers(guildID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMember(String playerID, String guildID) {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.DELETE_PLAYER.toString())){
            ps.setString(1, playerID);
            ps.executeUpdate();
            updateMembers(guildID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getGuildMembers(String id) {
        ArrayList<String> list = new ArrayList<>();
        for (Member m : guilds.get(id).getMembers()) {
            list.add(m.getId());
        }
        return list;
    }

    public ArrayList<String> getGuildAdvancedMembers(String id) {
        ArrayList<String> list = new ArrayList<>();
        for (Member m : guilds.get(id).getMembers()) {
            if (m.isAdvanced()) {
                list.add(m.getId());
            }
        }
        return list;
    }

    public HashMap<String, Guild> getGuilds() {
        return guilds;
    }

    private HashMap<String, Guild> getAllGuildsFromSQL() {
        HashMap<String, Guild> guilds = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(SQLCommand.GET_ALL_GUILDS.toString())){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Guild g = new Guild();
                g.setId(rs.getString("guild_id"));
                g.setGuildName(rs.getString("guild_name"));
                g.setChairman(rs.getString("guild_chairman"));
                g.setViceChairman1(rs.getString("guild_vice_chairman_1"));
                g.setViceChairman2(rs.getString("guild_vice_chairman_2"));
                g.setLevel(rs.getInt("guild_level"));
                g.setPoints(rs.getInt("guild_points"));
                g.setCash(rs.getInt("guild_cash"));
                g.setResidenceFLag(rs.getBoolean("guild_has_residence"));
                g.setHasChangedName(rs.getBoolean("guild_has_changed_name"));
                g.setMembers(getMembersFromSQL(g.getId()));
                guilds.put(g.getId(), g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guilds;
    }

    private ArrayList<Member> getMembersFromSQL(String guildID) {
        ArrayList<Member> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM `player_guild` WHERE guild_id = ?")){
            ps.setString(1, guildID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Member m = new Member(
                        rs.getString("player_id"),
                        rs.getString("guild_id"),
                        rs.getInt("player_contribution"),
                        rs.getBoolean("player_is_advanced")
                );
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void updateMembers(String guildID) {
        guilds.get(guildID).setMembers(getMembersFromSQL(guildID));
    }

    public void update() {
        guilds = getAllGuildsFromSQL();
    }


}
