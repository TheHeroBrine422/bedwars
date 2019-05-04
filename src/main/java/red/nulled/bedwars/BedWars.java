package red.nulled.bedwars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("helloworld")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
            //Player target = Bukkit.getServer().getPlayer(UUID.fromString("d03ea078-de10-4538-b696-b7fbbd2498e7"));
            sender.sendMessage("hello world!");
            return true;
        }
        return false;
    }
}
