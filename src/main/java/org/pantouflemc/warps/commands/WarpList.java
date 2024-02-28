package org.pantouflemc.warps.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pantouflemc.warps.Warps;

public class WarpTeleport implements CommandExecutor {

    private final Warps warps;

    public WarpTeleport(Warps warps) {
        this.warps = warps;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        String warpName = parseArguments(args);
        if(warpName == null){
            Component err_message = Component.text("You must specify a name for the warp. Use /warps.teleport <warpname>", NamedTextColor.RED);
            sender.sendMessage(err_message);
            return false;
        }
        OfflinePlayer player = (OfflinePlayer) sender;
        if(warps.teleportPlayer(player, warpName)){
            Component message =
                    Component.text("Teleported to warp ", NamedTextColor.GREEN)
                    .append(Component.text(warpName, NamedTextColor.WHITE))
                    .append(Component.text(".", NamedTextColor.GREEN));

            sender.sendMessage(message);
            return true;
        }
        Component alread_exists_warp_message = Component.text("Please enter a valid warp name. Use /warps.list to see existing warps.", NamedTextColor.RED);
        sender.sendMessage(alread_exists_warp_message);
        return false;
    }

    private @Nullable String parseArguments(String[] args) {
        if(args.length != 1){
            return null;
        }
        try{
            String warpName = args[0];
            return warpName;
        }
        catch (Exception e){
            return null;
        }
    }
}
