package de.tomjschwanke.mc.chuwu;

import java.sql.*;
import java.util.logging.Level;


public class ChuwuPlayerData {

    ChuwuConfig chuwuConfig = new ChuwuConfig();

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
        String createTableQuery = "CREATE TABLE IF NOT EXISTS `playerstates` ( `uuid` VARCHAR(36) NOT NULL , `state` BOOLEAN NOT NULL , PRIMARY KEY (`uuid`))";
        try (Connection connection = getDatabaseConnection()) {
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
        String insertQuery = "MERGE INTO `playerstates` (`uuid`, `state`) VALUES (?,?)";
        try (Connection connection = getDatabaseConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, uuid);
            insertStatement.setBoolean(2, state);
            insertStatement.executeUpdate();
        } catch (SQLException exception) {
            Chuwu.instance().getLogger().log(Level.SEVERE, "DB insert/update failed");
            Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
        }
    }

    void resetPlayerState(String uuid) {
        String deleteQuery = "DELETE FROM `playerstates` WHERE `uuid` LIKE ?";
        try(Connection connection = getDatabaseConnection()) {
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, uuid);
            deleteStatement.executeUpdate();
        }catch (SQLException exception) {
            Chuwu.instance().getLogger().log(Level.SEVERE, "DB delete failed");
            Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
        }
    }

    boolean getPlayerState(String uuid) {
        // Set state to playerdefault as fallback if player is not in DB
        boolean state = chuwuConfig.getPlayerDefault();
        String selectQuery = "SELECT state FROM `playerstates` WHERE `uuid` LIKE ?";
        try (Connection connection = getDatabaseConnection()) {
            PreparedStatement selectStatement;
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, uuid);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                state = resultSet.getBoolean("state");
            }
        } catch (SQLException exception) {
            Chuwu.instance().getLogger().log(Level.SEVERE, "DB query failed");
            Chuwu.instance().getLogger().log(Level.SEVERE, exception.getMessage());
        }
        return state;
    }
}
