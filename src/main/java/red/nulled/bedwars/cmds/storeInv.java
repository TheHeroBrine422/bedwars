package red.nulled.bedwars.cmds;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import red.nulled.bedwars.BedWars;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class storeInv implements CommandExecutor {

    Plugin plugin = BedWars.getPlugin(BedWars.class);

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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = (Player) sender; // In the long run this will not be the final solution to getting the world needed below for test.

        if (cmd.getName().equalsIgnoreCase("store")) {
            Inventory store = getServer().createInventory(target, 36, "Store");

            for (int i = 0; i < 35; i++) {
                if (plugin.getConfig().getString("shop.slot" + i) != null) {
                    shopItemAdd(Material.getMaterial(plugin.getConfig().getString("shop.slot" + i + ".item")), plugin.getConfig().getString("shop.slot" + i + ".itemName"), plugin.getConfig().getString("shop.slot" + i + ".currencyName"), plugin.getConfig().getInt("shop.slot" + i + ".price"), i, plugin.getConfig().getInt("shop.slot" + i + ".quantity"), store);
                }
            }

            target.openInventory(store);

            return true;
        }


            return false;
    }
}
