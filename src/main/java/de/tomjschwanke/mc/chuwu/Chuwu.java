package de.tomjschwanke.mc.chuwu;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class Chuwu extends JavaPlugin {

    @Override
    public void onEnable() {
        // bStats
        Metrics metrics = new Metrics(this, 10396);

        // Register chat event
        getServer().getPluginManager().registerEvents(new ChuwuEvents(), this);
    }

    @Override
    public void onDisable() {
    }
}
