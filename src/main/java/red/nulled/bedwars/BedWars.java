package red.nulled.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public final class BedWars extends JavaPlugin implements Listener {
    Map<String, Long> playersBought = new HashMap<String, Long>();
    Set<int[]> diamondBlocks = new HashSet<int[]>();
    Set<int[]> emeraldBlocks = new HashSet<int[]>();
    Map<String, int[]> purpurBlocks = new HashMap<String, int[]>();
    Map<String, String> teams = new HashMap<String, String>(); // Key- Player   Value-Team (LIME, RED, YELLOW, BLUE)
    String[] teamColors = new String[4];

    int centerXZ = 0; // config - area that it searches for diamond/emerald blocks
    int centerY = 40;
    int radiusXZ = 150;
    int radiusY = 4; // end of area config
    int dropHeight = 2; // height that items are dropped over the blocks
    int maxDiamondsEmeralds = 8; // max number of diamonds or emerald at a generator

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

    public void shopItemBuy(Material item, String name, Material currency, int price, int quantity, Player p) {
        int count = getCount(currency,p);
        if (count >= price) {
            p.sendMessage("Bought "+name); // send commands and sleep
            p.getInventory().remove(currency);
            p.getInventory().addItem(new ItemStack(currency, count - price));
            p.getInventory().addItem(new ItemStack(item,quantity));

            playersBought.put(p.getDisplayName(),System.currentTimeMillis());
        }
    }

    public int getCount (Material item, Player p) {
        int count = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack temp = p.getInventory().getItem(i);
            if (temp != null) { // thing is null if no item there.
                if (temp.getType() == item) {
                    count = count + temp.getAmount();
                }
            }
        }
        return count;
    }

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
        } else if (cmd.getName().equalsIgnoreCase("findgen")) {
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

                            Location color = test.add(0,-1,0);

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
                List<Entity> nearbyEntites = (List<Entity>) new Location(targetWorld,local[0],(local[1]+(dropHeight/2)),local[2]).getNearbyEntities(3,3,3);
                if (nearbyEntites.size() == 0) {
                    targetWorld.dropItem(new Location(targetWorld,local[0],(local[1]+dropHeight),local[2]), new ItemStack(dropMaterial));
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
        } else if (cmd.getName().equalsIgnoreCase("dropgen")) {
            Material spawn;

            if (args.length != 2) {
                return false;
            }

            if (args[0].equals("0") || args[0].equalsIgnoreCase("Iron")) {
                spawn = Material.IRON_INGOT;
            } else if (args[0].equals("1") || args[0].equalsIgnoreCase("Gold")) {
                spawn = Material.GOLD_INGOT;
            } else if (args[0].equals("2") || args[0].equalsIgnoreCase("Emerald")) {
                spawn = Material.EMERALD;
            } else {
                return false;
            }

            if (purpurBlocks.get(args[1].toUpperCase()) == null) {
                return false;
            }

            int[] local = purpurBlocks.get(args[1].toUpperCase());

            targetWorld.dropItem(new Location(targetWorld,local[0],(local[1]+dropHeight),local[2]), new ItemStack(spawn));

            return true;
        } else if (cmd.getName().equalsIgnoreCase("store")) {
            Inventory store = getServer().createInventory(target, 36, "Store");

            shopItemAdd(Material.IRON_SWORD, "Iron Sword", "Iron Ingots", 30, 0, 1, store);
            shopItemAdd(Material.WHITE_WOOL, "Wool", "Iron Ingots", 4, 1, 16, store);

            //Here opens the inventory
            target.openInventory(store);

            return true;
        } else if (cmd.getName().equalsIgnoreCase("setTeam")) {
            if (args.length != 2) {
                return false;
            }

            boolean online = false;
            boolean realTeamColor = false;

            Collection<? extends Player> c = Bukkit.getOnlinePlayers();
            for(Player player : c){
                if (player.getDisplayName().equals(args[0])) {
                    online = true;
                }
            }

            for (int i=0; i < teamColors.length; i++) {
                if (teamColors[i].equals(args[1])) {
                    realTeamColor = true;
                }
            }

            if (!online || !realTeamColor) {
                return false;
            }

            teams.put(args[0], args[1]);

            System.out.println("[BedWars] Teams: "+teams);

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
                    shopItemBuy(Material.IRON_SWORD, "Iron Sword", Material.IRON_INGOT, 30, 1, p);
                } else if (e.getSlot() == 1) {
                    shopItemBuy(Material.WHITE_WOOL, "Wool", Material.IRON_INGOT, 4, 16, p);
                }
            }
        }
    }

    @EventHandler
    public void autoRespawn(EntityDamageEvent event) {
        if (event.getEntity().getType().toString().equals("PLAYER")) {
            Player target = (Player) event.getEntity();
            if((target.getHealth()-event.getFinalDamage()) <= 0) {
                event.setCancelled(true);
                System.out.println("[BedWars] "+target.getDisplayName()+" has died!");
                target.teleport(new Location(target.getWorld(), 0.5, 47, 0.5));
                target.setHealth(20); // reset Health, Fire, & Potion Effects.    might need to add some more of these for resetting.
                target.setFireTicks(0);
                Collection<PotionEffect> potEffList = target.getActivePotionEffects();
                for(PotionEffect potEff : potEffList){
                    target.removePotionEffect(potEff.getType());
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        //e.setCancelled(true);
        Player target = e.getPlayer();
        String msg = e.getMessage();
        e.setFormat("%2$s");
        e.setMessage("[VI-47] ["+target.getDisplayName()+"] "+msg);
    }


}
