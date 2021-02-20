package de.tomjschwanke.mc.chuwu;

public class ChuwuConfig {

    void initConfig() {
        Chuwu.instance().getConfig().addDefault("globalstate", true);
        Chuwu.instance().getConfig().addDefault("playerdefault", false);
        Chuwu.instance().saveDefaultConfig();
    }
}
