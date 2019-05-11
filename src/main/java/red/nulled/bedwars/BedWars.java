package red.nulled.bedwars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import red.nulled.bedwars.events.*;
import red.nulled.bedwars.cmds.*;

public class BedWars extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("[BedWars] The server has started and the plugin is enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new storeBuy(), this);
        getServer().getPluginManager().registerEvents(new dmg(), this);
        getServer().getPluginManager().registerEvents(new chat(), this);

        getCommand("findDiamond").setExecutor(new diaEmeGens());
        getCommand("findEmerald").setExecutor(new diaEmeGens());
        getCommand("dropDiamond").setExecutor(new diaEmeGens());
        getCommand("dropEmerald").setExecutor(new diaEmeGens());
        getCommand("findgen").setExecutor(new baseGens());
        getCommand("dropgen").setExecutor(new baseGens());
        getCommand("store").setExecutor(new storeInv());

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("[BedWars] The server is stopping and the plugin is disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = (Player) sender; // In the long run this will not be the final solution to getting the world needed below for test.

        if (cmd.getName().equalsIgnoreCase("heal")) {
            target.setHealth(20);
            target.setFoodLevel(20);

            return true;
        } else if (cmd.getName().equalsIgnoreCase("test")) {
            System.out.println(getConfig().getString("shop.slot0"));

            return true;
        }

        return false;
    }
}

// test code/stuff i just wanna keep:
/*
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        System.out.println("[BedWars] Block Break   Player: "+p.getDisplayName()+"   Block: "+b.toString());

        //test code that i wanna keep for when we start adding block breaking mechanics if we do add those.
        if (b.getType() == Material.DIRT) {
            b.setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.DIAMOND));
            p.sendMessage("Diamond added to your inventory");
        }
    }
 */
