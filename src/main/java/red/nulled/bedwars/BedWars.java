package red.nulled.bedwars;

import org.bukkit.plugin.java.JavaPlugin;

public final class BedWars extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("The server has started and the plugin is enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // test comment
        System.out.println("The server is stopping and the plugin is disabled!");
    }
}
