package de.tomjschwanke.mc.chuwu;

import java.sql.*;
import java.util.logging.Level;
import org.h2.api.ErrorCode;

public class ChuwuPlayerData {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:file:" + Chuwu.instance().getDataFolder().getAbsolutePath() + "/playerstates;TRACE_LEVEL_FILE=0;TRACE_LEVEL_SYSTEM_OUT=0";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private static Connection getDatabaseConnection() {
        Connection databaseConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException exception) {
            Chuwu.instance().getLogger().log(Level.SEVERE, "DB Driver not found");
            Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
        }
        try {
            databaseConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException exception) {
            Chuwu.instance().getLogger().log(Level.SEVERE, "DB Connection failed");
            Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
        }
        return databaseConnection;
    }

    void initDatabase() {
        try (Connection connection = getDatabaseConnection()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS `playerstates` ( `uuid` VARCHAR(36) NOT NULL , `state` BOOLEAN NOT NULL)";
            Statement createTableStatement;
            createTableStatement = connection.createStatement();
            createTableStatement.executeUpdate(createTableQuery);
        } catch (SQLException exception) {
            if (exception.getErrorCode() == ErrorCode.TABLE_OR_VIEW_ALREADY_EXISTS_1) {
                Chuwu.instance().getLogger().log(Level.INFO, "Playerdata table already exists");
            } else {
                Chuwu.instance().getLogger().log(Level.SEVERE, "Playerdata table creation failed");
                Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
            }
        }
    }

    void savePlayerState(String uuid, boolean state) {
        try (Connection connection = getDatabaseConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement insertStatement;
            String InsertQuery = "MERGE INTO `playerstates`(`uuid`, `state`) VALUES(?,?)";
            insertStatement = connection.prepareStatement(InsertQuery);
            insertStatement.setString(1, uuid);
            insertStatement.setBoolean(2, state);
            insertStatement.executeUpdate();
            insertStatement.close();
            connection.commit();
        } catch (SQLException exception) {
            Chuwu.instance().getLogger().log(Level.SEVERE, "DB insert failed");
            Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
        }
    }

    boolean getPlayerState(String uuid) {
        // Set state to playerdefault as fallback if player is not in DB
        boolean state = Chuwu.instance().getConfig().getBoolean("playerdefault");
        try (Connection connection = getDatabaseConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement selectStatement;
            String selectQuery = "SELECT state FROM `playerstates` WHERE `uuid` LIKE ?";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, uuid);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                state = resultSet.getBoolean("state");
            }
            selectStatement.close();
            connection.commit();
        } catch (SQLException exception) {
            Chuwu.instance().getLogger().log(Level.SEVERE, "DB query failed");
            Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
        }
        return state;
    }
}
