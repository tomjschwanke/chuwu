package de.tomjschwanke.mc.chuwu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * ChuwuEvents implements the EventListener to listen for player-sent chat messages,
 * intercept them (so the original ones will never appear in chat),
 * then replace the characters to uwu-ify it and
 * replace the original message, making the modified on appear in chat.
 */
public class ChuwuEventListener implements Listener {
    ChuwuConfig chuwuConfig     = new ChuwuConfig();
    ChuwuPlayerData playerData  = new ChuwuPlayerData();
    // Event listener to intercept player-sent chat messages
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(chuwuConfig.getGlobalState() && playerData.getPlayerState(event.getPlayer())) {
            event.setMessage(event.getMessage().replace("R", "W").replace("L", "W").replace("r", "w").replace("l", "w"));
        }
    }
}
