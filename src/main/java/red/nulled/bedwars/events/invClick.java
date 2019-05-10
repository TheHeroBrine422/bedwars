package red.nulled.bedwars.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class invClick implements Listener {
    Map<String, Long> playersBought = new HashMap<String, Long>();


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

                if (e.getSlot() == 0) { // if 5th slot and its the help book do following
                    shopItemBuy(Material.IRON_SWORD, "Iron Sword", Material.IRON_INGOT, 30, 1, p);
                } else if (e.getSlot() == 1) {
                    shopItemBuy(Material.WHITE_WOOL, "Wool", Material.IRON_INGOT, 4, 16, p);
                }
            }
        }
    }
}
