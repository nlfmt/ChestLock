package avaze.chestlock;

import avaze.chestlock.commands.FriendCommand;
import avaze.chestlock.commands.LockCommand;
import avaze.chestlock.commands.ChestLockCommand;
import avaze.chestlock.commands.PluginCommand;
import avaze.chestlock.events.ChestBreakListener;
import avaze.chestlock.events.ChestOpenListener;
import avaze.chestlock.events.ChestPlaceListener;
import avaze.chestlock.events.ItemMoveListener;
import avaze.chestlock.util.ConfigFile;
import avaze.chestlock.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class ChestLock extends JavaPlugin {

    public static boolean enabled = true;
    private static ChestLock instance;
    public static ChestLock get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        ConfigFile.setPlugin(this);

        saveDefaultConfig();
        ChestLock.enabled = getConfig().getBoolean("enabled");

        String packageName = getClass().getPackage().getName();

        for (Class<?> clazz : new Reflections(packageName + ".commands").getSubTypesOf(PluginCommand.class)) {
            try {
                PluginCommand command = (PluginCommand) clazz.getDeclaredConstructor().newInstance();
                Objects.requireNonNull(getCommand(command.getCommandInfo().name())).setExecutor(command);
                for (String alias : command.getCommandInfo().aliases()) {
                    Objects.requireNonNull(getCommand(alias)).setExecutor(command);
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        for (Class<?> clazz : new Reflections(packageName + ".events").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDisable() {

    }

    public static void setLockEnabled(boolean enabled) {
        ChestLock.enabled = enabled;
        instance.getConfig().set("enabled", enabled);
        instance.saveConfig();
    }
}
