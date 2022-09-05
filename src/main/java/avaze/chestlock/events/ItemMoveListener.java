package avaze.chestlock.events;

import avaze.chestlock.ChestLock;
import avaze.chestlock.util.Util;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class ItemMoveListener implements Listener {

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent e) {
        if (!ChestLock.enabled) return;
        if (e.getSource().getHolder() instanceof Chest c) {
            if (Util.isLocked(c.getBlock().getLocation(), null)) {
                e.setCancelled(true);
            }
        }
    }
}
