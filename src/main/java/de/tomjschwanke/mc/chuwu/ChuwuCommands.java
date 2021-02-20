package de.tomjschwanke.mc.chuwu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChuwuCommands implements CommandExecutor {
    ChuwuConfig chuwuConfig = new ChuwuConfig();
    ChuwuPlayerData playerData = new ChuwuPlayerData();

    // TODO: add command and usage info to plugin.yml
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(command.getName().equals("chuwu")) {
                // Correct prefix
                // TODO: switch?
                if(args[0].equals("global")) {
                    // TODO: lock behind permission
                    if(args[1].equals("toggle")) {
                        // Global toggle
                        chuwuConfig.toggleGlobalState();
                    }else if(args[1].equals("on")) {
                        chuwuConfig.setGlobalState(true);
                    }else if(args[1].equals("off")) {
                        chuwuConfig.setGlobalState(false);
                    }
                }else if(args[0].equals("toggle")) {
                    // Toggle player state
                    playerData.savePlayerState(player.getUniqueId().toString(), !playerData.getPlayerState(player.getUniqueId().toString()));
                }else if(args[0].equals("on")) {
                    // Player-specific on
                    playerData.savePlayerState(player.getUniqueId().toString(), true);
                }else if(args[0].equals("off")) {
                    // Player-specific off
                    playerData.savePlayerState(player.getUniqueId().toString(), false);
                }
            }
        }

        // TODO: return true when command was valid
        return false;
    }
}
