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
        statement.execute("CREATE TABLE IF NOT EXISTS warps("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "x DOUBLE NOT NULL,"
                + "y DOUBLE NOT NULL,"
                + "z DOUBLE NOT NULL,"
                + "pitch DOUBLE NOT NULL,"
                + "yaw DOUBLE NOT NULL,"
                + "name VARCHAR(32) NOT NULL,"
                + "world VARCHAR(32) NOT NULL,"
                + "permission INTEGER NOT NULL" // 0 = authorized, 1 = closed
                + ");");
        statement.close();
    }

    /**
     * Create a new warp in the database
     *
     * @param x
     * @param y
     * @param z
     * @param pitch
     * @param yaw
     * @param name
     * @param world
     * @param permission
     * @throws SQLException
     */
    public void createWarp(double x, double y, double z, double pitch, double yaw, String name, String world, int permission) throws SQLException {

        //check if a warp with the same name already exists
        PreparedStatement statementCheck = connection.prepareStatement("SELECT * FROM warps WHERE name = ?;");
        statementCheck.setString(1, name);
        ResultSet resultSet = statementCheck.executeQuery();
        if (resultSet.next()) {
            throw new SQLException("A warp with the same name already exists.");
        }
        statementCheck.close();


        PreparedStatement statement = connection.prepareStatement("INSERT INTO warps (x, y, z, pitch, yaw, name, world, permission) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        statement.setDouble(1, x);
        statement.setDouble(2, y);
        statement.setDouble(3, z);
        statement.setDouble(4, pitch);
        statement.setDouble(5, yaw);
        statement.setString(6, name);
        statement.setString(7, world);
        statement.setInt(8, permission);
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating a warp failed, no rows affected.");
        }
        statement.close();
    }

    /**
     * Delete a warp from the database
     *
     * @param name
     * @throws SQLException
     */
    public void deleteWarp(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM warps WHERE name = ?;");
        statement.setString(1, name);
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Deleting a warp failed, no rows affected.");
        }
        statement.close();
    }

    /**
     * Create Location from a warp in the database
     *
     * @param name
     * @return Location of the warp
     * @throws SQLException
     */
    public @Nullable Location getWarp(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE name = ?;");
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            double x = resultSet.getDouble("x");
            double y = resultSet.getDouble("y");
            double z = resultSet.getDouble("z");
            double pitch = resultSet.getDouble("pitch");
            double yaw = resultSet.getDouble("yaw");
            String world = resultSet.getString("world");

            World wor = Bukkit.getWorld(world);
            if (wor != null) {
                return new Location(wor, x, y, z, (float) yaw, (float) pitch);
            }
        }
        statement.close();

        throw new SQLException("Warp not found.");
    }

    /**
     * Get all warps from the database
     *
     * @return List of all warps
     * @throws SQLException
     */
    public List<String> getWarps() throws SQLException {
        List<String> warps = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT name FROM warps;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            warps.add(resultSet.getString("name"));
        }
        statement.close();
        return warps;
    }

}
