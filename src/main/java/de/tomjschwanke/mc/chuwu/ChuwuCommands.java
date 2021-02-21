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
                // TODO: command feedback
                if(player.hasPermission("chuwu.toggle")) {
                    if(args.length == 1) {
                        switch(args[0]) {
                            case "toggle"   : playerData.savePlayerState(player.getUniqueId().toString(), !playerData.getPlayerState(player.getUniqueId().toString())); return true;
                            case "on"       : playerData.savePlayerState(player.getUniqueId().toString(), true); return true;
                            case "off"      : playerData.savePlayerState(player.getUniqueId().toString(), false); return true;
                            case "reset"    : playerData.resetPlayerState(player.getUniqueId().toString()); return true;
                            default         : return false;
                        }
                    }
                }
                if(player.hasPermission("chuwu.toggle.global")) {
                    if(args.length == 2 && args[0].equals("global")) {
                        switch(args[1]) {
                            case "toggle"   : chuwuConfig.setGlobalState(!chuwuConfig.getGlobalState()); return true;
                            case "on"       : chuwuConfig.setGlobalState(true); return true;
                            case "off"      : chuwuConfig.setGlobalState(false); return true;
                            default         : return false;
                        }
                    }
                }
                if(player.hasPermission("chuwu.toggle.others")) {
                    if(args.length == 2 && args[0].equals("player")) {
                        // TODO feedback
                        return false;
                    }else if(args.length == 3 && args[0].equals("player") && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
                        Player otherPlayer = Bukkit.getPlayer(args[1]);
                        // TODO: possible race condition where player loggs off right after check
                        if(otherPlayer != null) {
                            switch(args[2]) {
                                case "toggle"   : playerData.savePlayerState(otherPlayer.getUniqueId().toString(), !playerData.getPlayerState(otherPlayer.getUniqueId().toString())); return true;
                                case "on"       : playerData.savePlayerState(otherPlayer.getUniqueId().toString(), true); return true;
                                case "off"      : playerData.savePlayerState(otherPlayer.getUniqueId().toString(), false); return true;
                                case "reset"    : playerData.resetPlayerState(otherPlayer.getUniqueId().toString()); return true;
                                default         : return false;
                            }
                        }
                    }
                }
                if(player.hasPermission("chuwu.config")) {
                    if(args.length == 1) {
                        if ("reload".equals(args[0])) {
                            chuwuConfig.reloadConfig();
                        } else {
                            // TODO: feedback
                            return false;
                        }
                    }else if(args.length == 2 && args[0].equals("setplayerdefault")) {
                        switch(args[1]) {
                            case "toggle"   : chuwuConfig.setPlayerDefault(!chuwuConfig.getPlayerDefault()); return true;
                            case "on"       : chuwuConfig.setPlayerDefault(true); return true;
                            case "off"      : chuwuConfig.setPlayerDefault(false); return true;
                            default         : return false;
                        }
                    }
                }
            }
        }
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
