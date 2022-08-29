package avaze.chestlock.commands;

import avaze.chestlock.util.ConfigFile;
import avaze.chestlock.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p) {
            try {
                List<Block> blocks = p.getLineOfSight(null, 5);
                Block b = blocks.get(blocks.size() - 1);

                if (!Util.isLockable(b)) throw new Exception();

                ConfigFile chests = new ConfigFile("chests", ConfigFile.Type.SAVE_ONLY);
                String loc = Util.serialize(b.getLocation());

                if (command.getName().equals("lock")) {
                    if (Util.isLocked(b.getLocation(), p)) {
                        p.sendMessage("§cThis chest is already locked.");
                        return true;
                    }
                    chests.set(loc, p.getDisplayName());
                    chests.save();
                    sender.sendMessage("§aChest locked!");
                } else if (command.getName().equals("unlock")) {
                    if (chests.contains(loc)) {
                        String owner = chests.getString(loc);
                        if (owner == null || !owner.equals(p.getDisplayName())) {
                            p.sendMessage("§cThis chest was not locked by you!");
                            return true;
                        }
                        chests.set(loc, null);
                        chests.save();
                        sender.sendMessage("§aChest unlocked!");
                    } else {
                        sender.sendMessage("§cChest is not locked!");
                        return true;
                    }
                }

            } catch (NullPointerException e) {
                sender.sendMessage("§cSomething went wrong :/");
                return true;
            } catch (Exception e) {
                sender.sendMessage("§cYou are not looking at a chest!");
                return true;
            }
        }

        return true;
    }
}
