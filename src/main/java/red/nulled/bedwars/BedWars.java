package red.nulled.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;

import java.util.*;

public final class BedWars extends JavaPlugin implements Listener {
    Map<String, Long> playersBought = new HashMap<String, Long>();
    Set<int[]> diamondBlocks = new HashSet<int[]>();
    Set<int[]> emeraldBlocks = new HashSet<int[]>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("[BedWars] The server has started and the plugin is enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // test comment
        System.out.println("[BedWars] The server is stopping and the plugin is disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = Bukkit.getServer().getPlayer(sender.getName()); // In the long run this will not be the final solution to getting the world needed below for test.
        World targetWorld = target.getWorld();

        int dropHeight = 2; // config - height that the emeralds/diamonds are dropped over the blocks

        if (cmd.getName().equalsIgnoreCase("finddiamond") || cmd.getName().equalsIgnoreCase("findemerald")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
            Material findMaterial;
            String material;
            Set<int[]> blocks = new HashSet<int[]>();

            if (cmd.getName().equalsIgnoreCase("finddiamond")) {
                findMaterial = Material.DIAMOND_BLOCK;
                material = "Diamond";
            } else {
                findMaterial = Material.EMERALD_BLOCK;
                material = "Emerald";
            }

            System.out.println("[BedWars] Finding "+material+" Blocks");

            int centerXZ = 0; // config - area that it searches for diamond/emerald blocks
            int centerY = 40;
            int radiusXZ = 100;
            int radiusY = 4; // config - end

            int startXZ = centerXZ-radiusXZ;
            int startY = centerY-radiusY;
            int rangeXZ = radiusXZ*2+1;
            int rangeY = radiusY*2+1;

            Location test = new Location(targetWorld, startXZ, startY, startXZ);
            for (int x = startXZ; x<(startXZ+rangeXZ); x++) {
                for (int z = startXZ; z<(startXZ+rangeXZ); z++) {
                    for (int y = startY; y<(startY+rangeY); y++) {
                        test.set(x,y,z);
                        if (test.getBlock().getType() == findMaterial) {
                            int[] local = new int[3];
                            local[0] = x;
                            local[1] = y;
                            local[2] = z;

                            blocks.add(local);
                            System.out.println("[BedWars] "+x+" "+y+" "+z+" "+material+" Block Found");
                        }
                    }
                }
            }
            if (material.equals("Diamond")) {
                diamondBlocks = blocks;
            } else {
                emeraldBlocks = blocks;
            }

            return true;
        } else if (cmd.getName().equalsIgnoreCase("dropdiamond")) {
            for (int[] local : diamondBlocks) {
                targetWorld.dropItem(new Location(targetWorld,local[0],(local[1]+dropHeight),local[2]), new ItemStack(Material.DIAMOND));
            }

            return true;
        } else if (cmd.getName().equalsIgnoreCase("dropemerald")) {
            for (int[] local : emeraldBlocks) {
                targetWorld.dropItem(new Location(targetWorld,local[0],(local[1]+dropHeight),local[2]), new ItemStack(Material.EMERALD));
            }

            return true;
        } else if (cmd.getName().equalsIgnoreCase("store")) {
            Inventory store = getServer().createInventory(target, 36, "Store");

            //Here you define our item
            ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
            ItemMeta ironSwordMeta = ironSword.getItemMeta();
            ArrayList<String> lore= new ArrayList<String>();

            lore.add(" ");
            lore.add("Cost: 30 iron");

            ironSwordMeta.setLore(lore);
            ironSwordMeta.setDisplayName("Iron Sword");

            ironSword.setItemMeta(ironSwordMeta);
            store.setItem(0, ironSword);

            //Here opens the inventory
            target.openInventory(store);

            return true;
        }

    return false;

        /* some test code that i wanna keep
        Block targetBlock = target.getTargetBlock(100);
        if (targetBlock.getType() == Material.DIRT) {
            target.sendMessage("It's Dirt!");
        }
        sender.sendMessage("hello world!");*/
    }

    /*@EventHandler
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
    }*/

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (playersBought.get(p.getDisplayName()) == null) {
            playersBought.put(p.getDisplayName(),(long) 0);
        }

        if (e.getView().getTitle().equalsIgnoreCase("Store")) {
            e.setCancelled(true); // stop from picking up item
            if (playersBought.get(p.getDisplayName())+100 < System.currentTimeMillis()) {

                if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) { // sees what they grabbed. If nothing do nothing
                    return;
                }

                if (e.getSlot() == 0) { // if 5th slot and its the help book do following
                    int ironCount = 0;
                    for (int i = 0; i < 36; i++) {
                        ItemStack temp = p.getInventory().getItem(i);
                        if (temp != null) { // thing is null if no item there.
                            if (temp.getType() == Material.IRON_INGOT) {
                                ironCount = ironCount + temp.getAmount();
                            }
                        }
                    }
                    if (ironCount >= 30) {
                        p.sendMessage("Bought Iron Sword " + ironCount); // send commands and sleep
                        p.getInventory().remove(Material.IRON_INGOT);
                        p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, ironCount - 30));
                        p.getInventory().addItem(new ItemStack(Material.IRON_SWORD));

                        playersBought.put(p.getDisplayName(),System.currentTimeMillis());
                    }
                }
            }
        }
    }
}
