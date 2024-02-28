package org.pantouflemc.warps;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import org.pantouflemc.warps.database.DatabaseManager;

public final class Warps extends JavaPlugin {

    private static DatabaseManager databaseManager;
    private static Warps instance;
    private static Logger logger;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = this.getLogger();
        databaseManager = new DatabaseManager();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.disconnect();
    }


}
