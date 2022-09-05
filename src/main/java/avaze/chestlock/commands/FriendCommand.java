package avaze.chestlock.commands;

import avaze.chestlock.util.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        final String USAGE = "§cUsage: /friend <add|remove|list> <player>";

        if (args.length == 0) {
            sender.sendMessage(USAGE);
            return true;
        }

        switch (args[0]) {
            case "add" -> {
                if (args.length < 2) {
                    sender.sendMessage(USAGE);
                    return true;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage("§cPlayer not found!");
                    return true;
                }
                if (p.getName().equals(sender.getName())) {
                    sender.sendMessage("§cYou can't add yourself!");
                    return true;
                }
                ConfigFile friends = new ConfigFile("friends", ConfigFile.Type.SAVE_ONLY);
                if (friends.getStringList(sender.getName()).contains(p.getName())) {
                    sender.sendMessage(p.getName() + "§c is already your friend!");
                    return true;
                }
                List<String> friendList = friends.getStringList(sender.getName());
                friendList.add(p.getName());
                friends.set(sender.getName(), friendList);
                friends.save();
                sender.sendMessage("§d" + p.getName() + "§f is now your friend!");
                p.sendMessage("§d" + sender.getName() + "§f added you as a friend! You can now open their chests!");

            }
            case "remove" -> {
                if (args.length < 2) {
                    sender.sendMessage(USAGE);
                    return true;
                }
                ConfigFile friends = new ConfigFile("friends", ConfigFile.Type.SAVE_ONLY);
                if (!friends.getStringList(sender.getName()).contains(args[1])) {
                    sender.sendMessage(args[1] + "§c is not your friend!");
                    return true;
                }
                List<String> friendList = friends.getStringList(sender.getName());
                friendList.remove(args[1]);
                friends.set(sender.getName(), friendList);
                friends.save();
                sender.sendMessage("§d" + args[1] + "§f is no longer your friend!");
                Player p = Bukkit.getPlayer(args[1]);
                if (p != null)
                    p.sendMessage("§d" + sender.getName() + "§f removed you as a friend! You can no longer open their chests!");
            }
            case "list" -> {
                ConfigFile friends = new ConfigFile("friends", ConfigFile.Type.SAVE_ONLY);
                List<String> friendList = friends.getStringList(sender.getName());
                if (friendList.isEmpty()) {
                    sender.sendMessage("§cYou don't have any friends!");
                    return true;
                }
                sender.sendMessage("§fYour friends: ");
                sender.sendMessage("- §d" + String.join("§f, §d", friendList));
            }
            default -> sender.sendMessage(USAGE);
        }

        return true;
    }
}
