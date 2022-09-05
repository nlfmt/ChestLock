package avaze.chestlock.events;

import avaze.chestlock.util.Util;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class ChestMoveListener implements Listener {

    private void onPistonMove(List<Block> blocks, BlockPistonEvent e) {
        boolean locked = blocks.stream()
                .anyMatch((Block b) -> Util.isLocked(b.getLocation(), null));

        if (locked) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        onPistonMove(e.getBlocks(), e);
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        onPistonMove(e.getBlocks(), e);
    }
}
