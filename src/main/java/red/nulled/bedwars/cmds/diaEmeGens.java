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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class diaEmeGens implements CommandExecutor {
    Set<int[]> diamondBlocks = new HashSet<int[]>();
    Set<int[]> emeraldBlocks = new HashSet<int[]>();

    int centerXZ = 0; // config - area that it searches for diamond/emerald blocks
    int centerY = 40;
    int radiusXZ = 150;
    int radiusY = 4; // end of area config
    int dropHeight = 2; // height that items are dropped over the blocks
    int maxDiamondsEmeralds = 8; // max number of diamonds or emerald at a generator

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = (Player) sender; // In the long run this will not be the final solution to getting the world needed below for test.
        World targetWorld = target.getWorld();

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

            System.out.println("[BedWars] Finding " + material + " Blocks");

            int startXZ = centerXZ - radiusXZ;
            int startY = centerY - radiusY;
            int rangeXZ = radiusXZ * 2 + 1;
            int rangeY = radiusY * 2 + 1;

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

                            blocks.add(local);
                            System.out.println("[BedWars] " + x + " " + y + " " + z + " " + material + " Block Found");
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
        } else if (cmd.getName().equalsIgnoreCase("dropemerald") || cmd.getName().equalsIgnoreCase("dropdiamond")) {
            Set<int[]> blocks;
            Material dropMaterial;
            if (cmd.getName().equalsIgnoreCase("dropemerald")) {
                blocks = emeraldBlocks;
                dropMaterial = Material.EMERALD;
            } else {
                blocks = diamondBlocks;
                dropMaterial = Material.DIAMOND;
            }

            for (int[] local : blocks) {
                List<Entity> nearbyEntites = (List<Entity>) new Location(targetWorld, local[0], (local[1] + (dropHeight / 2)), local[2]).getNearbyEntities(3, 3, 3);
                if (nearbyEntites.size() == 0) {
                    targetWorld.dropItem(new Location(targetWorld, local[0], (local[1] + dropHeight), local[2]), new ItemStack(dropMaterial));
                } else {
                    int count = 0;
                    for (int i = 0; i < nearbyEntites.size(); i++) {
                        if (nearbyEntites.get(i).getType().toString().equals("DROPPED_ITEM")) {
                            Item temp = (Item) nearbyEntites.get(i);
                            ItemStack tempStack = temp.getItemStack();
                            if (tempStack.getType() == dropMaterial) {
                                count = count + tempStack.getAmount();
                            }
                        }
                    }
                    if (count < maxDiamondsEmeralds) {
                        targetWorld.dropItem(new Location(targetWorld, local[0], (local[1] + dropHeight), local[2]), new ItemStack(dropMaterial));
                    }
                }
            }

            return true;
        }

        return false;
    }
}
