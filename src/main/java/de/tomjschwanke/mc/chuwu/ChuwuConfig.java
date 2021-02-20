package de.tomjschwanke.mc.chuwu;

public class ChuwuConfig {

    void initConfig() {
        Chuwu.inst().getConfig().addDefault("globalstate", true);
        Chuwu.inst().getConfig().addDefault("playerdefault", false);
        Chuwu.inst().saveDefaultConfig();
    }
}
