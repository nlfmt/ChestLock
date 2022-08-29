package avaze.chestlock.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Util {
    public static String serialize(Location l) {
        World world = l.getWorld();
        return (world == null ? "<unknown_world>" : world.getName()) + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public static boolean isLocked(Location l, Player p) {
        if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) return false;
        ConfigFile chests = new ConfigFile("chests");
        String owner = chests.getString(Util.serialize(l));
        return (owner != null && !owner.equals(p.getName()));
    }
    public static String getOwner(Location l) {
        ConfigFile chests = new ConfigFile("chests");
        return chests.getString(Util.serialize(l));
    }
    public static boolean isLockable(Block b) {
        return (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST);
    }
}
