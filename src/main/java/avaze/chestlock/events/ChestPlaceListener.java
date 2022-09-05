package avaze.chestlock.events;

import avaze.chestlock.ChestLock;
import avaze.chestlock.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ChestPlaceListener implements Listener {

    @EventHandler
    public void onChestPlace(BlockPlaceEvent e) {
        if (!ChestLock.enabled) return;
        Block b = e.getBlock();

        if (b.getState() instanceof Chest c) {
            System.out.println("Chest placed at " + b.getLocation());
            Bukkit.getScheduler().runTaskLater(ChestLock.get(), () -> {

                Chest otherChest = Util.getConnectedChest(c);
                if (otherChest == null) return;


                if (Util.isLocked(otherChest.getLocation(), e.getPlayer())) {

                    Block rightSide = otherChest.getBlock();

                    BlockData leftData = b.getBlockData();
                    BlockData rightData = rightSide.getBlockData();

                    org.bukkit.block.data.type.Chest chestDataLeft = (org.bukkit.block.data.type.Chest) leftData;
                    chestDataLeft.setType(org.bukkit.block.data.type.Chest.Type.SINGLE);
                    b.setBlockData(chestDataLeft);

                    org.bukkit.block.data.type.Chest chestDataRight = (org.bukkit.block.data.type.Chest) rightData;
                    chestDataRight.setType(org.bukkit.block.data.type.Chest.Type.SINGLE);
                    rightSide.setBlockData(chestDataRight);
                }

            }, 1);
        }
    }
}