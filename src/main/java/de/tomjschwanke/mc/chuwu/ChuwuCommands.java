package de.tomjschwanke.mc.chuwu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        ArrayList<String> list = new ArrayList<>();
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(command.getName().equals("chuwu")) {
                if(player.hasPermission("chuwu.toggle")) {
                    if(args.length == 1) {
                        list.add("toggle");
                        list.add("on");
                        list.add("off");
                        list.add("reset");
                    }
                }
                if(player.hasPermission("chuwu.toggle.global")) {
                    if(args.length == 1) {
                        list.add("global");
                    }else if(args.length == 2 && args[0].equals("global")) {
                        list.add("toggle");
                        list.add("on");
                        list.add("off");
                    }
                }
                if(player.hasPermission("chuwu.toggle.others")) {
                    if(args.length == 1) {
                        list.add(("player"));
                    }else if(args.length == 2 && args[0].equals("player")) {
                        for(Player addPlayer : Bukkit.getOnlinePlayers()) {
                            list.add(addPlayer.getName());
                        }
                    }else if(args.length == 3 && args[0].equals("player") && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
                        list.add("toggle");
                        list.add("on");
                        list.add("off");
                        list.add("reset");
                    }
                }
                if(player.hasPermission("chuwu.config")) {
                    if(args.length == 1) {
                        list.add("reload");
                        list.add("setplayerdefault");
                    }else if(args.length == 2 && args[0].equals("setplayerdefault")) {
                        list.add("toggle");
                        list.add("on");
                        list.add("off");
                    }
                }
            }
        }
        return list.stream().filter(string -> string.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }
}
