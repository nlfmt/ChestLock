package avaze.chestlock.events;

import avaze.chestlock.ChestLock;
import avaze.chestlock.util.ConfigFile;
import avaze.chestlock.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestOpenListener implements Listener {

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        if (!ChestLock.enabled) return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null) {
            if (Util.isLockable(e.getClickedBlock())) {
                if (Util.isLocked(e.getClickedBlock().getLocation(), e.getPlayer())) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("§cThis chest is owned by " + Util.getOwner(e.getClickedBlock().getLocation()) + "!");
                }
            }
        }
    }

}
