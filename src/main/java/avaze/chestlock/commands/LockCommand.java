package avaze.chestlock.commands;

import avaze.chestlock.ChestLock;
import avaze.chestlock.util.ConfigFile;
import avaze.chestlock.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender instanceof Player p) {
            try {
                // Get the block the player is looking at
//                List<Block> blocks = p.getLineOfSight(null, 5);
//                Block b = blocks.get(blocks.size() - 1);
                Block b = p.getTargetBlockExact(5);

                if (b == null || !Util.isLockable(b)) throw new Exception();

                ConfigFile chests = new ConfigFile("chests", ConfigFile.Type.SAVE_ONLY);

                Chest chest = (Chest) b.getState();
                Chest otherChestBlock = Util.getConnectedChest((Chest) b.getState());
                Location otherChest = otherChestBlock != null ? otherChestBlock.getLocation() : null;
                // Get Locations to lock
                String loc = Util.serialize(b.getLocation());
                String loc2 = otherChest == null ? null : Util.serialize(otherChest);


                // #### LOCK ####
                if (command.getName().equals("lock")) {
                    if (Util.isLocked(b.getLocation(), null)) {
                        String owner = Util.getOwner(b.getLocation());
                        p.sendMessage("§cThis chest is already locked by §f" + owner);
                        return true;
                    }
                    Util.lock(p.getName(), loc, loc2);

                    chest.setCustomName("§d§l" + p.getName() + "§r's Chest");
                    chest.update();

                    sender.sendMessage("§aChest locked!");
                    ChestLock.get().getLogger().info(p.getName() + " locked chest at " + loc);


                // #### UNLOCK ####
                } else if (command.getName().equals("unlock")) {
                    if (chests.contains(loc)) {
                        String owner = chests.getString(loc);
                        if (owner == null || !owner.equals(p.getName())) {
                            p.sendMessage("§cThis chest was locked by §f" + owner);
                            return true;
                        }
                        Util.unlock(loc, loc2);

                        // Reset names
                        chest.setCustomName(null);
                        chest.update();

                        sender.sendMessage("§aChest unlocked!");
                        ChestLock.get().getLogger().info(p.getName() + " unlocked a chest at " + loc);
                    } else {
                        sender.sendMessage("§cChest is not locked!");
                        return true;
                    }


                // #### FORCE LOCK ####
                } else if (command.getName().equals("forcelock") && p.isOp()) {
                    String playerName = args[0] == null ? p.getName() : args[0];
                    Util.lock(playerName, loc, loc2);

                    chest.setCustomName("§d§l" + playerName + "§r's Chest");
                    chest.update();

                    sender.sendMessage("§aChest locked!");


                // #### FORCE UNLOCK ####
                } else if (command.getName().equals("forceunlock") && p.isOp()) {
                    Util.unlock(loc, loc2);

                    chest.setCustomName(null);
                    chest.update();

                    sender.sendMessage("§aChest unlocked!");
                }

            } catch (NullPointerException e) {
                sender.sendMessage("§cSomething went wrong :/");
                e.printStackTrace();
                return true;
            } catch (Exception e) {
                sender.sendMessage("§cYou are not looking at a chest!");
                return true;
            }
        }

        return true;
    }
}
