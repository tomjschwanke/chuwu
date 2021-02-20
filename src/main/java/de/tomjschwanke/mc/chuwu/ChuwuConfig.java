package de.tomjschwanke.mc.chuwu;

public class ChuwuConfig {

    void initConfig() {
        Chuwu.instance().getConfig().addDefault("globalstate", true);
        Chuwu.instance().getConfig().addDefault("playerdefault", false);
        Chuwu.instance().saveDefaultConfig();
    }

    void toggleGlobalState() {
        setGlobalState(!getGlobalState());
    }

    void togglePlayerDefault() {
        setPlayerDefault(!getPlayerDefault());
    }

    void setGlobalState(boolean state) {
        Chuwu.instance().getConfig().set("globalstate", state);
        Chuwu.instance().saveConfig();
    }

    void setPlayerDefault(boolean state) {
        Chuwu.instance().getConfig().set("playerdefault", state);
        Chuwu.instance().saveConfig();
    }

    boolean getGlobalState() {
        return Chuwu.instance().getConfig().getBoolean("globalstate");
    }

    boolean getPlayerDefault() {
        return Chuwu.instance().getConfig().getBoolean("playerdefault");
    }
}
