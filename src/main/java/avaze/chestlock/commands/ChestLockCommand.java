package avaze.chestlock.commands;

import avaze.chestlock.ChestLock;
import avaze.chestlock.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "chestlock", requiresPlayer = false)
public class ChestLockCommand extends PluginCommand {
    @Override
    public void execute(CommandSender sender, Command command, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /chestlock <on|off>");
            return;
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
    }
}
