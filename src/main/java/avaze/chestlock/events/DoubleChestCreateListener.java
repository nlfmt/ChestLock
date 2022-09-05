package avaze.chestlock.events;

import avaze.chestlock.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

public class DoubleChestCreateListener implements Listener {

    @EventHandler
    public void onDoubleChestCreate(BlockPlaceEvent e) {
        BlockState b = e.getBlock().getState();
        // TODO: run 1 tick later
        if (b instanceof Chest c) {
            System.out.println("Chest placed");
            Inventory inv = c.getInventory();
            if (inv instanceof DoubleChestInventory dinv) {
                System.out.println("chest is doublechest");
                DoubleChest d = dinv.getHolder();
                if (Util.isLocked(d.getRightSide().getInventory().getLocation(), e.getPlayer())
                    || Util.isLocked(d.getLeftSide().getInventory().getLocation(), e.getPlayer())) {
                    System.out.println("connects to locked chest");
                    e.setCancelled(true);
                }
            }
        }
    }
}
