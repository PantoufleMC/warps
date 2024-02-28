package org.pantouflemc.warps.database;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager() {
        connect();
    }

    /**
     * Connect to the database
     */
    private void connect() {
        // TODO: Change SQLite to PostgreSQL
        File databaseDirectory = new File("plugins/Warps");
        File databaseFile = new File(databaseDirectory, "database.db");

        // Create the path to the database if it doesn't exist
        if (!databaseDirectory.exists()) {
            databaseDirectory.mkdirs();
        }

        // Register Driver Class, this should never fail
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getPath());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Initialize the database
        try {
            initialization();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect from the database
     */
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the database
     */
    private void initialization() throws SQLException {
        Statement statement = connection.createStatement();

        // Create the warps table
        statement.execute("CREATE TABLE IF NOT EXISTS warps ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "x DOUBLE NOT NULL"
                + "y DOUBLE NOT NULL"
                + "z DOUBLE NOT NULL"
                + "pitch DOUBLE NOT NULL"
                + "yaw DOUBLE NOT NULL"
                + "name VARCHAR(32) NOT NULL"
                + "world VARCHAR(32) NOT NULL"
                + "permission INTEGER NOT NULL" // 0 = authorized, 1 = closed
                + ");");
        statement.close();
    }

}
