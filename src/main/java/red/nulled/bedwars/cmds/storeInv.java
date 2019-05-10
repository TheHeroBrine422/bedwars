package red.nulled.bedwars.cmds;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class storeInv implements CommandExecutor {
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

            shopItemAdd(Material.IRON_SWORD, "Iron Sword", "Iron Ingots", 30, 0, 1, store);
            shopItemAdd(Material.WHITE_WOOL, "Wool", "Iron Ingots", 4, 1, 16, store);

            target.openInventory(store);

            return true;
        }


            return false;
    }
}
