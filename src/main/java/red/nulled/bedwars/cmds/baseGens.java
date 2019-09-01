package red.nulled.bedwars.cmds;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import red.nulled.bedwars.BedWars;

import java.util.*;

public class baseGens implements CommandExecutor {
    Map<String, int[]> purpurBlocks = new HashMap<String, int[]>();
    String[] teamColors = new String[4];
    Plugin plugin = BedWars.getPlugin(BedWars.class);

    int centerXZ = 0; // config - area that it searches for diamond/emerald blocks
    int centerY = 40;
    int radiusXZ = 150;
    int radiusY = 4; // end of area config
    int dropHeight = 2; // height that items are dropped over the blocks

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = (Player) sender; // In the long run this will not be the final solution to getting the world needed below for test.
        World targetWorld = target.getWorld();

        if (cmd.getName().equalsIgnoreCase("findgen")) {
            Material findMaterial = Material.PURPUR_BLOCK;

            Map<String, int[]> blocks = new HashMap<String, int[]>();

            int startXZ = centerXZ - radiusXZ;
            int startY = centerY - radiusY;
            int rangeXZ = radiusXZ * 2 + 1;
            int rangeY = radiusY * 2 + 1;

            System.out.println("[BedWars] Finding Purpur Blocks");
            int counter = 0;
            Location test = new Location(targetWorld, startXZ, startY, startXZ);
            for (int x = startXZ; x < (startXZ + rangeXZ); x++) {
                for (int z = startXZ; z < (startXZ + rangeXZ); z++) {
                    for (int y = startY; y < (startY + rangeY); y++) {
                        test.set(x, y, z);
                        if (test.getBlock().getType() == findMaterial) {
                            int[] local = new int[3];
                            local[0] = x;
                            local[1] = y;
                            local[2] = z;

                            Location color = test.add(0, -1, 0);

                            blocks.put(color.getBlock().getType().toString().split("_")[0], local);
                            if (counter <= 3) {
                                teamColors[counter] = color.getBlock().getType().toString().split("_")[0];
                            }
                            counter++;
                            System.out.println("[BedWars] " + x + " " + y + " " + z + " " + color.getBlock().getType().toString().split("_")[0] + " Purpur Block Found");
                        }
                    }
                }
            }

            purpurBlocks = blocks;

            System.out.println(blocks);
            System.out.println(purpurBlocks);

            return true;
        } else if (cmd.getName().equalsIgnoreCase("dropgen")) {
            Material spawn;
            int max;

            if (args.length != 2) {
                return false;
            }

            if (args[0].equals("0") || args[0].equalsIgnoreCase("Iron")) {
                spawn = Material.IRON_INGOT;
                max = Integer.parseInt(plugin.getConfig().getString("gensMax.base.iron"));
            } else if (args[0].equals("1") || args[0].equalsIgnoreCase("Gold")) {
                spawn = Material.GOLD_INGOT;
                max = Integer.parseInt(plugin.getConfig().getString("gensMax.base.gold"));
            } else if (args[0].equals("2") || args[0].equalsIgnoreCase("Emerald")) {
                max = Integer.parseInt(plugin.getConfig().getString("gensMax.base.emerald"));
                spawn = Material.EMERALD;
            } else {
                return false;
            }

            if (purpurBlocks.get(args[1].toUpperCase()) == null) {
                System.out.println("fail find" + " " + args[1].toUpperCase() + " " + purpurBlocks.get(args[1].toUpperCase()));
                return false;
            }

            int[] local = purpurBlocks.get(args[1].toUpperCase());

            List<Entity> nearbyEntites = (List<Entity>) new Location(targetWorld, local[0], (local[1] + (dropHeight / 2)), local[2]).getNearbyEntities(3, 3, 3);
            if (nearbyEntites.size() == 0) {
                targetWorld.dropItem(new Location(targetWorld, local[0], (local[1] + dropHeight), local[2]), new ItemStack(spawn));
            } else {
                int count = 0;
                for (int i = 0; i < nearbyEntites.size(); i++) {
                    if (nearbyEntites.get(i).getType().toString().equals("DROPPED_ITEM")) {
                        Item temp = (Item) nearbyEntites.get(i);
                        ItemStack tempStack = temp.getItemStack();
                        if (tempStack.getType() == spawn) {
                            count = count + tempStack.getAmount();
                        }
                    }
                }
                if (count < max) {
                    targetWorld.dropItem(new Location(targetWorld, local[0], (local[1] + dropHeight), local[2]), new ItemStack(spawn));
                }
            }

            return true;
        }

        return false;
    }
}
