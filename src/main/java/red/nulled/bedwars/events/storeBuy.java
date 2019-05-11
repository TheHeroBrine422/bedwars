package red.nulled.bedwars.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import red.nulled.bedwars.BedWars;

import java.util.HashMap;
import java.util.Map;

public class storeBuy implements Listener {
    Map<String, Long> playersBought = new HashMap<String, Long>();
    Plugin plugin = BedWars.getPlugin(BedWars.class);


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

                for (int i = 0; i < 35; i++) {
                    if (plugin.getConfig().getString("shop.slot" + i) != null && e.getSlot() == i) {
                        shopItemBuy(Material.getMaterial(plugin.getConfig().getString("shop.slot" + i + ".item")), plugin.getConfig().getString("shop.slot" + i + ".itemName"), Material.getMaterial(plugin.getConfig().getString("shop.slot" + i + ".currency")), plugin.getConfig().getInt("shop.slot" + i + ".price"), plugin.getConfig().getInt("shop.slot" + i + ".quantity"), p);
                    }
                }

            }
        }
    }
}
