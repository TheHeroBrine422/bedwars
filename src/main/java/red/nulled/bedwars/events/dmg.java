package red.nulled.bedwars.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class dmg implements Listener {
    @EventHandler
    public void autoRespawn(EntityDamageEvent event) {
        if (event.getEntity().getType().toString().equals("PLAYER")) {
            Player target = (Player) event.getEntity();
            System.out.println(target.getDisplayName());
            if((target.getHealth()-event.getFinalDamage()) <= 0) {
                event.setCancelled(true);
                System.out.println("[BedWars] "+target.getDisplayName()+" has died!");
                target.teleport(new Location(target.getWorld(), 0.5, 47, 0.5));
                target.setHealth(20); // reset Health, Fire, & Potion Effects.    might need to add some more of these for resetting.
                target.setFireTicks(10);
                Collection<PotionEffect> potEffList = target.getActivePotionEffects();
                for(PotionEffect potEff : potEffList){
                    target.removePotionEffect(potEff.getType());
                }
            }
        }
    }
}
