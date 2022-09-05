package avaze.chestlock;

import avaze.chestlock.commands.LockCommand;
import avaze.chestlock.events.ChestBreakListener;
import avaze.chestlock.events.ChestOpenListener;
import avaze.chestlock.util.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChestLock extends JavaPlugin {

    private static ChestLock instance;
    public static ChestLock get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        ConfigFile.setPlugin(this);

        getCommand("lock").setExecutor(new LockCommand());
        getCommand("unlock").setExecutor(new LockCommand());

        getServer().getPluginManager().registerEvents(new ChestBreakListener(), this);
        getServer().getPluginManager().registerEvents(new ChestOpenListener(), this);


        // TODO: lock double chest automatically
        // TODO: when a chest ist placed, check if it is connected to a locked chest and lock it if so or cancel it
        // TODO: block hopper, hopper minecart
        // TODO: block locked chests from being moved by pistons
        // TODO: make sure chest cant burn, explode etc
    }

    @Override
    public void onDisable() {

    }
}
