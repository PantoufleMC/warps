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

public class WarpList implements CommandExecutor {

    private final Warps warps;

    public WarpList(Warps warps) {
        this.warps = warps;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        boolean warpName = parseArguments(args);
        if(warpName == false){
            Component err_message = Component.text("Wrong syntax. Use /warps.list", NamedTextColor.RED);
            sender.sendMessage(err_message);
            return false;
        }
        OfflinePlayer player = (OfflinePlayer) sender;
        if(warps.getWarps(player)){

            return true;
        }
        Component alread_exists_warp_message = Component.text("No warps exists.", NamedTextColor.RED);
        sender.sendMessage(alread_exists_warp_message);
        return false;
    }

    private @Nullable boolean parseArguments(String[] args) {
        if(args.length != 0){
            return false;
        }
        return true;
    }
}
