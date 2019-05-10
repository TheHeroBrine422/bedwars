package red.nulled.bedwars.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        //e.setCancelled(true);
        Player target = e.getPlayer();
        String msg = e.getMessage();
        e.setFormat("%2$s");
        e.setMessage("[VI-47] ["+target.getDisplayName()+"] "+msg);
    }
}
