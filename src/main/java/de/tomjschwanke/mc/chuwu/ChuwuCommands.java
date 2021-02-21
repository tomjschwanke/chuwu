package de.tomjschwanke.mc.chuwu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChuwuCommands implements CommandExecutor, TabCompleter {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // TODO: implement tab completion
        // TODO: filter list by already entered stuff
        ArrayList<String> list = new ArrayList<>();
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(command.getName().equals("chuwu")) {
                if(player.hasPermission("chuwu.toggle") && !args[0].matches("on|off|toggle") && args[1].isEmpty()) {
                    list.add("toggle");
                    list.add("on");
                    list.add("off");
                }
                if(player.hasPermission("chuwu.toggle.global")) {
                    if(!args[0].equals("global") && args[1].isEmpty()) {
                        list.add("global");
                    }else if(args[0].equals("global") && !args[1].matches("on|off|toggle") && args[2].isEmpty()) {
                        list.add("toggle");
                        list.add("on");
                        list.add("off");
                    }
                }
                if(player.hasPermission("chuwu.toggle.others")) {
                    if(!args[0].equals("player") && args[1].isEmpty()) {
                        list.add(("player"));
                    }else if(args[0].equals("player") && !Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1])) && args[2].isEmpty()) {
                        // TODO: filter online players by already entered stuff
                        for(Player addPlayer : Bukkit.getOnlinePlayers()) {
                            list.add(addPlayer.getName());
                        }
                    }else if(args[0].equals("player") && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1])) && !args[2].matches("on|off|toggle") && args[3].isEmpty()) {
                        list.add("toggle");
                        list.add("on");
                        list.add("off");
                    }
                }
            }
        }
        return list;
    }
}
