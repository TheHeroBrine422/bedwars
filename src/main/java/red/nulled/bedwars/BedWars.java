package red.nulled.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;

import java.util.HashSet;
import java.util.UUID;

public final class BedWars extends JavaPlugin implements Listener {
    HashSet diaBlocks = new HashSet();

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("[BedWars] The server has started and the plugin is enabled!");
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
        if (cmd.getName().equalsIgnoreCase("finddiamond") || cmd.getName().equalsIgnoreCase("findemerald")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
            Player target = Bukkit.getServer().getPlayer(sender.getName()); // In the long run this will not be the final solution to getting the world needed below for test.
            Material findMaterial;
            String material;

            if (cmd.getName().equalsIgnoreCase("finddiamond")) {
                findMaterial = Material.DIAMOND_BLOCK;
                material = "Diamond";
            } else {
                findMaterial = Material.EMERALD_BLOCK;
                material = "Emerald";
            }
            diaBlocks = new HashSet();
            System.out.println("[BedWars] Finding "+material+" Blocks");
            int startXZ = -150;
            int startY = 36;
            int rangeXZ = 301;
            int rangeY = 9;
            Location test = new Location(target.getWorld(), startXZ, startY, startXZ);
            for (int x = startXZ; x<(startXZ+rangeXZ); x++) {
                for (int z = startXZ; z<(startXZ+rangeXZ); z++) {
                    for (int y = startY; y<(startY+rangeY); y++) {
                        test.set(x,y,z);
                        if (test.getBlock().getType() == findMaterial) {
                            int[] local = new int[3];
                            local[0] = x;
                            local[1] = y;
                            local[2] = z;

                            diaBlocks.add(local);
                            System.out.println("[BedWars] "+x+" "+y+" "+z+" Diamond Block Found");
                        }
                    }
                }
            }

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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        System.out.println("[BedWars] Block Break   Player: "+p.getDisplayName()+"   Block: "+b.toString());

        /*test code that i wanna keep for when we start adding block breaking mechanics if we do add those.
        if (b.getType() == Material.DIRT) {
            b.setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.DIAMOND));
            p.sendMessage("Diamond added to your inventory");
        }*/
    }
}
