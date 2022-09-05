package avaze.chestlock.events;

import avaze.chestlock.ChestLock;
import avaze.chestlock.util.ConfigFile;
import avaze.chestlock.util.Util;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class ChestBreakListener implements Listener {

    @EventHandler
    public void onChestBreak(BlockBreakEvent e) {
        if (!ChestLock.enabled) return;
        if (e.isCancelled()) return;

        if (Util.isLockable(e.getBlock())) {
            if (Util.isLocked(e.getBlock().getLocation(), e.getPlayer())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cThis chest is owned by §f" + Util.getOwner(e.getBlock().getLocation()) + "§c!");
            } else {
                ConfigFile chests = new ConfigFile("chests", ConfigFile.Type.SAVE_ONLY);
                chests.set(Util.serialize(e.getBlock().getLocation()), null);
                chests.save();
            }
        }
    }

    private void onChestExplode(boolean cancelled, List<Block> blocks) {
        if (cancelled) return;

        ArrayList<Block> bl = new ArrayList<>(blocks);
        for (Block b : bl) {
            if (Util.isLockable(b) && Util.isLocked(b.getLocation(), null)) {
                blocks.remove(b);
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        onChestExplode(e.isCancelled(), e.blockList());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        onChestExplode(e.isCancelled(), e.blockList());
    }

}
