package avaze.chestlock.commands;

import avaze.chestlock.ChestLock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChestLockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /chestlock <on|off>");
            return true;
        }

        if (args[0].equals("on")) {
            ChestLock.setLockEnabled(true);
            sender.sendMessage("ChestLock is now §aenabled§f!");
        } else if (args[0].equals("off")) {
            ChestLock.setLockEnabled(false);
            sender.sendMessage("ChestLock is now §cdisabled§f!");
        } else {
            sender.sendMessage("§cUsage: /chestlock <on|off>");
        }

        return true;
    }
}
