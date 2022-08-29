package avaze.chestlock.events;

import avaze.chestlock.util.ConfigFile;
import avaze.chestlock.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ChestBreakListener implements Listener {

    @EventHandler
    public void onChestBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        if (Util.isLockable(e.getBlock())) {
            if (Util.isLocked(e.getBlock().getLocation(), e.getPlayer())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cThis chest is owned by " + Util.getOwner(e.getBlock().getLocation()) + "!");
            } else {
                ConfigFile chests = new ConfigFile("chests", ConfigFile.Type.SAVE_ONLY);
                chests.set(Util.serialize(e.getBlock().getLocation()), null);
                chests.save();
            }
        }
    }
}
