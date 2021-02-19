package de.tomjschwanke.mc.chuwu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChuwuEvents implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        message = message.replace("R", "W");
        message = message.replace("L", "W");
        message = message.replace("r", "w");
        message = message.replace("l", "w");
        event.setMessage(message);
    }
}
