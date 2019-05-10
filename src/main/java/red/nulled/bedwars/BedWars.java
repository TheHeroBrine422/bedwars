package red.nulled.bedwars;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import red.nulled.bedwars.events.*;
import red.nulled.bedwars.cmds.*;

import java.util.*;

public class BedWars extends JavaPlugin implements Listener {
    Map<String, int[]> purpurBlocks = new HashMap<String, int[]>();
    Map<String, String> teams = new HashMap<String, String>(); // Key- Player   Value-Team (LIME, RED, YELLOW, BLUE)

    public void shopItemAdd(Material item, String name, String currency, int price, int slot, int quantity, Inventory store) {
        ItemStack itemStack = new ItemStack(item, quantity);
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore= new ArrayList<String>();

        lore.add(" ");
        lore.add("Cost: "+price+" "+currency);

        itemMeta.setLore(lore);
        itemMeta.setDisplayName(name);

        itemStack.setItemMeta(itemMeta);
        store.setItem(slot, itemStack);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("[BedWars] The server has started and the plugin is enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new invClick(), this);
        getServer().getPluginManager().registerEvents(new dmg(), this);
        getServer().getPluginManager().registerEvents(new chat(), this);

        getCommand("findDiamond").setExecutor(new diaEmeGens());
        getCommand("findEmerald").setExecutor(new diaEmeGens());
        getCommand("dropDiamond").setExecutor(new diaEmeGens());
        getCommand("dropEmerald").setExecutor(new diaEmeGens());
        getCommand("findgen").setExecutor(new baseGens());
        getCommand("dropgen").setExecutor(new baseGens());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // test comment
        System.out.println("[BedWars] The server is stopping and the plugin is disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = (Player) sender; // In the long run this will not be the final solution to getting the world needed below for test.

        if (cmd.getName().equalsIgnoreCase("store")) {
            Inventory store = getServer().createInventory(target, 36, "Store");

            shopItemAdd(Material.IRON_SWORD, "Iron Sword", "Iron Ingots", 30, 0, 1, store);
            shopItemAdd(Material.WHITE_WOOL, "Wool", "Iron Ingots", 4, 1, 16, store);

            target.openInventory(store);

            return true;
        } else if (cmd.getName().equalsIgnoreCase("heal")) {
            target.setHealth(20);
            target.setFoodLevel(20);

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
