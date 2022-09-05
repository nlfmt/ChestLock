package avaze.chestlock;

import avaze.chestlock.commands.FriendCommand;
import avaze.chestlock.commands.LockCommand;
import avaze.chestlock.commands.ChestLockCommand;
import avaze.chestlock.events.ChestBreakListener;
import avaze.chestlock.events.ChestOpenListener;
import avaze.chestlock.events.ChestPlaceListener;
import avaze.chestlock.events.ItemMoveListener;
import avaze.chestlock.util.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

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

        Objects.requireNonNull(getCommand("lock")).setExecutor(new LockCommand());
        Objects.requireNonNull(getCommand("unlock")).setExecutor(new LockCommand());
        Objects.requireNonNull(getCommand("forcelock")).setExecutor(new LockCommand());
        Objects.requireNonNull(getCommand("forceunlock")).setExecutor(new LockCommand());
        Objects.requireNonNull(getCommand("friend")).setExecutor(new FriendCommand());
        Objects.requireNonNull(getCommand("chestlock")).setExecutor(new ChestLockCommand());

        getServer().getPluginManager().registerEvents(new ChestBreakListener(), this);
        getServer().getPluginManager().registerEvents(new ChestOpenListener(), this);
        getServer().getPluginManager().registerEvents(new ChestPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new ItemMoveListener(), this);


        // TODO: block hopper, hopper minecart
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
