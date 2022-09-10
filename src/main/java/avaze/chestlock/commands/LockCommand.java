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

@CommandInfo(name = "lock", requiresPlayer = true, aliases = {"unlock", "forcelock", "forceunlock"})
public class LockCommand extends PluginCommand {

    @Override
    public void execute(Player player, Command command, String[] args) {

        try {
            // Get the block the player is looking at
            Block b = player.getTargetBlockExact(5);
            if (b == null || !Util.isLockable(b)) throw new Exception();

            Chest chest = (Chest) b.getState();
            Chest otherChestBlock = Util.getConnectedChest((Chest) b.getState());
            // Get Locations to lock
            Location loc = b.getLocation();
            Location loc2 = otherChestBlock != null ? otherChestBlock.getLocation() : null;


            // #### LOCK ####
            if (command.getName().equals("lock")) {
                if (Util.isLocked(b.getLocation(), null)) {
                    String owner = Util.getOwner(b.getLocation());
                    player.sendMessage("§cThis chest is already locked by §f" + owner);
                    Util.playFailSound(player);
                    return;
                }
                Util.lock(player.getName(), loc, loc2);

                chest.setCustomName("§d§l" + player.getName() + "§r's Chest");
                chest.update();

                player.sendMessage("§aChest locked!");
                Util.playSuccessSound(player);
                ChestLock.get().getLogger().info(player.getName() + " locked chest at " + loc);


            // #### UNLOCK ####
            } else if (command.getName().equals("unlock")) {
                if (Util.isLocked(loc, null)) {
                    if (Util.isLocked(loc, player)) {
                        player.sendMessage("§cThis chest was locked by §f" + Util.getOwner(loc));
                        Util.playFailSound(player);
                        return;
                    }
                    Util.unlock(loc, loc2);

                    // Reset names
                    chest.setCustomName(null);
                    chest.update();
                    if (otherChestBlock != null) {
                        otherChestBlock.setCustomName(null);
                        otherChestBlock.update();
                    }

                    player.sendMessage("§aChest unlocked!");
                    Util.playSuccessSound(player);
                    ChestLock.get().getLogger().info(player.getName() + " unlocked a chest at " + loc);
                } else {
                    player.sendMessage("§cChest is not locked!");
                    Util.playFailSound(player);
                }


            // #### FORCE LOCK ####
            } else if (command.getName().equals("forcelock") && player.isOp()) {
                String playerName = args[0] == null ? player.getName() : args[0];
                Util.lock(playerName, loc, loc2);

                chest.setCustomName("§d§l" + playerName + "§r's Chest");
                chest.update();

                player.sendMessage("§aChest locked!");
                Util.playSuccessSound(player);


            // #### FORCE UNLOCK ####
            } else if (command.getName().equals("forceunlock") && player.isOp()) {
                Util.unlock(loc, loc2);

                chest.setCustomName(null);
                chest.update();

                player.sendMessage("§aChest unlocked!");
                Util.playSuccessSound(player);
            }

        } catch (NullPointerException e) {
            player.sendMessage("§cSomething went wrong :/");
            Util.playFailSound(player);
            e.printStackTrace();
        } catch (Exception e) {
            player.sendMessage("§cYou are not looking at a chest!");
            Util.playFailSound(player);
        }
    }
}
