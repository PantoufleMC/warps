package org.pantouflemc.warps.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;
import org.pantouflemc.warps.Warps;
public class WarpCreate implements CommandExecutor {

    private final Warps warps;

    public WarpCreate(Warps warps) {
        this.warps = warps;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        String warpName = parseArguments(args);
        if(warpName == null){
            Component err_message = Component.text("You must specify a name for the warp. Use /warps.create <warpname>", NamedTextColor.RED);
            sender.sendMessage(err_message);
            return false;
        }
        OfflinePlayer player = (OfflinePlayer) sender;
        if(warps.createWarp(player, warpName)){
            Component message =
                    Component.text("Warp ", NamedTextColor.GREEN)
                    .append(Component.text(warpName, NamedTextColor.WHITE))
                    .append(Component.text(" created.", NamedTextColor.GREEN));

            sender.sendMessage(message);
            return true;
        }
        Component alread_exists_warp_message = Component.text("A warp with the name ", NamedTextColor.RED)
                .append(Component.text(warpName, NamedTextColor.WHITE))
                .append(Component.text(" already exists.", NamedTextColor.RED));
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
