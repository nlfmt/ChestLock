package avaze.chestlock.commands;

import avaze.chestlock.util.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandInfo(name = "friend", requiresPlayer = true)
public class FriendCommand extends PluginCommand {

    @Override
    public void execute(Player player, Command command, String[] args) {

        final String USAGE = "§cUsage: /friend <add|remove|list> <player>";

        if (args.length == 0) {
            player.sendMessage(USAGE);
            return;
        }

        switch (args[0]) {
            case "add" -> {
                if (args.length < 2) {
                    player.sendMessage(USAGE);
                    return;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    player.sendMessage("§cPlayer not found!");
                    return;
                }
                if (p.getName().equals(player.getName())) {
                    player.sendMessage("§cYou can't add yourself!");
                    return;
                }
                ConfigFile friends = new ConfigFile("friends", ConfigFile.Type.SAVE_ONLY);
                if (friends.getStringList(player.getName()).contains(p.getName())) {
                    player.sendMessage(p.getName() + "§c is already your friend!");
                    return;
                }
                List<String> friendList = friends.getStringList(player.getName());
                friendList.add(p.getName());
                friends.set(player.getName(), friendList);
                friends.save();
                player.sendMessage("§d" + p.getName() + "§f is now your friend!");
                p.sendMessage("§d" + player.getName() + "§f added you as a friend! You can now open their chests!");

            }
            case "remove" -> {
                if (args.length < 2) {
                    player.sendMessage(USAGE);
                    return;
                }
                ConfigFile friends = new ConfigFile("friends", ConfigFile.Type.SAVE_ONLY);
                if (!friends.getStringList(player.getName()).contains(args[1])) {
                    player.sendMessage(args[1] + "§c is not your friend!");
                    return;
                }
                List<String> friendList = friends.getStringList(player.getName());
                friendList.remove(args[1]);
                friends.set(player.getName(), friendList);
                friends.save();
                player.sendMessage("§d" + args[1] + "§f is no longer your friend!");
                Player p = Bukkit.getPlayer(args[1]);
                if (p != null)
                    p.sendMessage("§d" + player.getName() + "§f removed you as a friend! You can no longer open their chests!");
            }
            case "list" -> {
                ConfigFile friends = new ConfigFile("friends", ConfigFile.Type.SAVE_ONLY);
                List<String> friendList = friends.getStringList(player.getName());
                if (friendList.isEmpty()) {
                    player.sendMessage("§cYou don't have any friends!");
                    return;
                }
                player.sendMessage("§fYour friends: ");
                player.sendMessage("- §d" + String.join("§f, §d", friendList));
            }
            default -> player.sendMessage(USAGE);
        }
    }
}
