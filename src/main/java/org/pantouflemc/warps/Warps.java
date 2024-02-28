package org.pantouflemc.warps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
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
        try{
            databaseManager = new DatabaseManager();
        }
        catch (Exception e) {
            logger.info(e.getMessage());
        }

        this.getCommand("warps.create").setExecutor(new org.pantouflemc.warps.commands.WarpCreate(this));
        this.getCommand("warps.teleport").setExecutor(new org.pantouflemc.warps.commands.WarpTeleport(this));
        this.getCommand("warps.delete").setExecutor(new org.pantouflemc.warps.commands.WarpDelete(this));
        this.getCommand("warps.list").setExecutor(new org.pantouflemc.warps.commands.WarpList(this));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.disconnect();
    }


    /*
     * The following methods are used to interact with the database.
     */

    /**
     * Create a new warp in the database.
     * @param player The player who created the warp (command sender).
     * @param warpName The name of the warp.
     */
    public boolean createWarp(OfflinePlayer player, String warpName){
        Location location = player.getPlayer().getLocation();
        try {
            databaseManager.createWarp(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), warpName, location.getWorld().getName(), 0);
        }
        catch (Exception e) {
            getLogger().info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Delete a warp from the database.
     * @param warpName The name of the warp to delete.
     */
    public boolean deleteWarp(String warpName){
        try {
            databaseManager.deleteWarp(warpName);
            return true;
        }
        catch (Exception e) {
            getLogger().info(e.getMessage());
            return false;
        }
    }

    /**
     * Teleport a player to a warp.
     * @param player The player to teleport.
     * @param warpName The name of the warp.
     */

    public boolean teleportPlayer(OfflinePlayer player, String warpName){
        try {
            Location location = databaseManager.getWarp(warpName);
            player.getPlayer().teleport(location);
            return true;
        }
        catch (Exception e) {
            getLogger().info(e.getMessage());
            return false;
        }
    }

    /**
     * Get the list of warps.
     * @return The list of warps.
     */
    public boolean getWarps(OfflinePlayer player) {
        try {
            List<String> warpList = databaseManager.getWarps();
            String warps = String.join(", ", warpList);

            Component message =
                    Component.text("List of warps: ", NamedTextColor.YELLOW)
                            .append(Component.text(warps, NamedTextColor.WHITE))
                            .append(Component.text(".", NamedTextColor.YELLOW));

            Player pla = (Player) player;
            pla.sendMessage(message);
            return true;
        } catch (Exception e) {
            getLogger().info(e.getMessage());
            return false;
        }
    }
}
