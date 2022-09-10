package avaze.chestlock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class PluginCommand implements CommandExecutor {

    private final CommandInfo commandInfo;

    public PluginCommand() {
        this.commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(commandInfo, "Commands must have a CommandInfo annotation");
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!commandInfo.permission().isEmpty() && !sender.hasPermission(commandInfo.permission())) {
            sender.sendMessage("§cYou do not have permission to use this command");
            return true;
        }

        if (commandInfo.requiresPlayer()) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command can only be used by players");
                return true;
            }
            execute((Player) sender, command, args);
            return true;
        }

        execute(sender, command, args);
        return true;
    }

    public void execute(Player player, Command command, String[] args) {}
    public void execute(CommandSender sender, Command command, String[] args) {}
}
