package de.tomjschwanke.mc.chuwu;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class Chuwu extends JavaPlugin {

    private static Chuwu instance;

    @Override
    public void onEnable() {
        // Instance for other classes
        instance = this;

        // bStats
        Metrics metrics = new Metrics(this, 10396);

        // Init config
        ChuwuConfig config = new ChuwuConfig();
        config.initConfig();

        // Register chat event
        getServer().getPluginManager().registerEvents(new ChuwuEvents(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Chuwu instance() {
        return instance;
    }
}
