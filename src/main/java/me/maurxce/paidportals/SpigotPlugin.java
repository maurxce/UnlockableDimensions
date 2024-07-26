package me.maurxce.paidportals;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotPlugin extends JavaPlugin {
    public void disable() {
        getServer().getPluginManager().disablePlugin(this);
    }
}
