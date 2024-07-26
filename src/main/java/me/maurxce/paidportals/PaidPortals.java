package me.maurxce.paidportals;

import lombok.Getter;
import md.schorn.spigothelper.configuration.Config;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PaidPortals extends JavaPlugin {

    public static PaidPortals getInstance() {
        return getPlugin(PaidPortals.class);
    }

    private Config config;
    private Config lang;

    @Override
    public void onEnable() {
        this.config = new Config("config.yml", this);
        this.lang = new Config("lang.yml", this);
    }

    @Override
    public void onDisable() {

    }
}
