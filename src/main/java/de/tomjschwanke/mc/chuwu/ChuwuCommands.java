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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(command.getName().equals("chuwu")) {
                switch(args.length) {
                    case 1:
                        switch(args[0]) {
                            case "toggle":
                                if(player.hasPermission("chuwu.toggle")) {
                                    playerData.savePlayerState(player.getUniqueId().toString(), !playerData.getPlayerState(player.getUniqueId().toString()));
                                    player.sendMessage("Chuwu toggled to " + (playerData.getPlayerState(player.getUniqueId().toString()) ? "on" : "off" ));
                                    return true;
                                }else {
                                    player.sendMessage("You do not have permission to set chuwu for yourself");
                                    return false;
                                }
                            case "on":
                                if(player.hasPermission("chuwu.toggle")) {
                                    playerData.savePlayerState(player.getUniqueId().toString(), true);
                                    player.sendMessage("Chuwu set to on");
                                    return true;
                                }else {
                                    player.sendMessage("You do not have permission to set chuwu for yourself");
                                    return false;
                                }
                            case "off":
                                if(player.hasPermission("chuwu.toggle")) {
                                    playerData.savePlayerState(player.getUniqueId().toString(), false);
                                    player.sendMessage("Chuwu set to off");
                                    return true;
                                }else {
                                    player.sendMessage("You do not have permission to set chuwu for yourself");
                                    return false;
                                }
                            case "reset":
                                if(player.hasPermission("chuwu.toggle")) {
                                    playerData.resetPlayerState(player.getUniqueId().toString());
                                    player.sendMessage("Chuwu reset to playerdefault for you: " + (playerData.getPlayerState(player.getUniqueId().toString()) ? "on" : "off"));
                                    return true;
                                }else {
                                    player.sendMessage("You do not have the permission to set chuwu for yourself");
                                    return false;
                                }
                            case "reload":
                                if(player.hasPermission("chuwu.config")) {
                                    chuwuConfig.reloadConfig();
                                    player.sendMessage("Chuwu config reloaded");
                                    return true;
                                }else {
                                    player.sendMessage("You do not have permission to reload the chuwu config");
                                    return false;
                                }
                            default:
                                player.sendMessage("Usage: /chuwu [toggle | on | off | reset]");
                                return false;
                        }
                    case 2:
                        switch(args[0]) {
                            case "global":
                                switch(args[1]) {
                                    case "toggle":
                                        if(player.hasPermission("chuwu.toggle.global")) {
                                            chuwuConfig.toggleGlobalState();
                                            player.sendMessage("Chuwu gloablly toggled to " + (chuwuConfig.getGlobalState() ? "on" : "off"));
                                            return true;
                                        }else {
                                            player.sendMessage("You do not have permission to set chuwu globally");
                                            return false;
                                        }
                                    case "on":
                                        if(player.hasPermission("chuwu.toggle.global")) {
                                            chuwuConfig.setGlobalState(true);
                                            player.sendMessage("Chuwu globally set to on");
                                            return true;
                                        }else {
                                            player.sendMessage("You do not have permission to set chuwu globally");
                                            return false;
                                        }
                                    case "off":
                                        if(player.hasPermission("chuwu.toggle.global")) {
                                            chuwuConfig.setGlobalState(false);
                                            player.sendMessage("Chuwu globally set to off");
                                            return true;
                                        }else {
                                            player.sendMessage("You do not have permission to set chuwu gloablly");
                                            return false;
                                        }
                                    default:
                                        if(player.hasPermission("chuwu.toggle.global")) {
                                            player.sendMessage("Usage: /chuwu global [toggle | on | off]");
                                        }else {
                                            player.sendMessage("Usage: /chuwu [toggle | on | off | reset]");
                                        }
                                        return false;
                                }
                            case "player":
                                if(player.hasPermission("chuwu.toggle.others")) {
                                    player.sendMessage("Usage: /chuwu player {player} [toggle | on | off | reset]");
                                }else {
                                    player.sendMessage("You do not have permission to set chuwu for other players");
                                }
                                return false;
                            case "setplayerdefault":
                                switch(args[1]) {
                                    case "toggle":
                                        if(player.hasPermission("chuwu.config")) {
                                            chuwuConfig.togglePlayerDefault();
                                            player.sendMessage("Chuwu playerdefault set to " + (chuwuConfig.getPlayerDefault() ? "on" : "off"));
                                            return true;
                                        }else {
                                            player.sendMessage("You do not have permission to adjust the playerdefault");
                                            return false;
                                        }
                                    case "on":
                                        if(player.hasPermission("chuwu.config")) {
                                            chuwuConfig.setPlayerDefault(true);
                                            player.sendMessage("Chuwu playerdefault set to on");
                                            return true;
                                        }else {
                                            player.sendMessage("You do not have permission to adjust the playerdefault");
                                            return false;
                                        }
                                    case "off":
                                        if(player.hasPermission("chuwu.config")) {
                                            chuwuConfig.setPlayerDefault(false);
                                            player.sendMessage("Chuwu playerdefault set to off");
                                            return true;
                                        }else {
                                            player.sendMessage("You do not have permission to adjust the playerdefault");
                                            return false;
                                        }
                                    default:
                                        if(player.hasPermission("chuwu.config")) {
                                            player.sendMessage("Usage: /chuwu setplayerdefault [toggle | on | off]");
                                        }else {
                                            player.sendMessage("Usage: /chuwu [toggle | on | off | reset]");
                                        }
                                        return false;
                                }
                            default:
                                if(player.hasPermission("chuwu.config")) {
                                    player.sendMessage("Usage: /chuwu setplayerdefault [toggle | on | off]");
                                }else {
                                    player.sendMessage("Usage: /chuwu [toggle | on | off | reset]");
                                }
                                return false;
                        }
                    case 3:
                        switch(args[0]) {
                            case "player":
                                if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
                                    // player found
                                    Player otherPlayer = Bukkit.getPlayer(args[1]);
                                    if(otherPlayer != null) {
                                        switch(args[2]) {
                                            case "toggle":
                                                if(player.hasPermission("chuwu.toggle.others")) {
                                                    playerData.savePlayerState(otherPlayer.getUniqueId().toString(), !playerData.getPlayerState(otherPlayer.getUniqueId().toString()));
                                                    player.sendMessage("Chuwu set to " + (playerData.getPlayerState(otherPlayer.getUniqueId().toString())? "on" : "off") + " for " + otherPlayer.getDisplayName() + " (" + player.getName() + ")");
                                                    return true;
                                                }else {
                                                    player.sendMessage("You do not have permission to set chuwu for other players");
                                                    return false;
                                                }
                                            case "on":
                                                if(player.hasPermission("chuwu.toggle.others")) {
                                                    playerData.savePlayerState(otherPlayer.getUniqueId().toString(), true);
                                                    player.sendMessage("Chuwu set to on for " + otherPlayer.getDisplayName() + " (" + player.getName() + ")");
                                                    return true;
                                                }else {
                                                    player.sendMessage("You do not have permission to set chuwu for other players");
                                                    return false;
                                                }
                                            case "off":
                                                if(player.hasPermission("chuwu.toggle.others")) {
                                                    playerData.savePlayerState(otherPlayer.getUniqueId().toString(), false);
                                                    player.sendMessage("Chuwu set to off for " + otherPlayer.getDisplayName() + " (" + player.getName() + ")");
                                                    return true;
                                                }else {
                                                    player.sendMessage("You do not have permission to set chuwu for other players");
                                                    return false;
                                                }
                                            case "reset":
                                                if(player.hasPermission("chuwu.toggle.others")) {
                                                    playerData.resetPlayerState(otherPlayer.getUniqueId().toString());
                                                    player.sendMessage("Chuwu reset to playerdefault for " + player.getDisplayName() + " (" + player.getName() + ")");
                                                    return true;
                                                }else {
                                                    player.sendMessage("You do not have permission to set chuwu for others");
                                                    return false;
                                                }
                                            default:
                                                if(player.hasPermission("chuwu.toggle.others")) {
                                                    player.sendMessage("Usage: /chuwu player {player} [toggle | on | off | reset]");
                                                }else {
                                                    player.sendMessage("Usage: /chuwu [toggle | on | off | reset]");
                                                }
                                        }
                                    }else {
                                        player.sendMessage("The player could not be found. Is he online?");
                                    }
                                }else {
                                    if(player.hasPermission("chuwu.toggle.others")) {
                                        player.sendMessage("The player could not be found. Is he online?");
                                    }else {
                                        player.sendMessage("You do not have permission to set chuwu for other players");
                                    }
                                    return false;
                                }
                            default:
                                if(player.hasPermission("chuwu.toggle.others")) {
                                    player.sendMessage("Usage: /chuwu player {player} [toggle | on | off | reset]");
                                }else {
                                    player.sendMessage("Usage: /chuwu [toggle | on | off | reset]");
                                }
                        }
                }
                player.sendMessage("Usage: /chuwu [toggle | on | off | reset]");
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
