package avaze.chestlock.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nullable;

public class Util {
    public static String serialize(Location l) {
        World world = l.getWorld();
        return (world == null ? "<unknown_world>" : world.getName()) + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public static Chest getConnectedChest(Chest c) {
        Inventory inv = c.getInventory();
        if (inv instanceof DoubleChestInventory dinv) {
            DoubleChest d = dinv.getHolder();

            if (d==null) return null;
            Chest leftChest = (Chest) d.getLeftSide();
            Chest rightChest = (Chest) d.getRightSide();
            if (leftChest == null || rightChest == null) return null;
            Location loc1 = leftChest.getLocation();
            Location loc2 = rightChest.getLocation();
            if (loc1.equals(c.getLocation())) {
                return rightChest;
            } else {
                return leftChest;
            }
        } else {
            return null;
        }
    }

    public static boolean isLocked(Location l, Player p) {
        if (p != null && (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)) return false;
        ConfigFile chests = new ConfigFile("chests", ConfigFile.Type.SAVE_ONLY);
        String owner = chests.getString(Util.serialize(l));

        if (owner == null) return false;
        if (p == null) return true;
        if (owner.equals(p.getName())) return false;
        // test if player is in friend list
        ConfigFile friends = new ConfigFile("friends", ConfigFile.Type.SAVE_ONLY);
        return friends.getStringList(owner).stream().noneMatch(friend -> friend.equals(p.getName()));
    }
    public static String getOwner(Location l) {
        ConfigFile chests = new ConfigFile("chests", ConfigFile.Type.SAVE_ONLY);
        return chests.getString(Util.serialize(l));
    }
    public static boolean isLockable(Block b) {
        return (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST);
    }

    public static void lock( @Nullable String playerName, String... locs) {
        ConfigFile chests = new ConfigFile("chests", ConfigFile.Type.SAVE_ONLY);
        for (String loc : locs) {
            if (loc != null) chests.set(loc, playerName);
        }
        chests.save();
    }
    public static void unlock(String... locs) {
        lock(null, locs);
    }
}
