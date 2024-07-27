package me.maurxce.paidportals.repository;

import lombok.Getter;
import md.schorn.spigothelper.configuration.Config;
import org.bukkit.World;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class SettingsRepository {
    private final Map<World.Environment, Boolean> poolEnabled;
    private final Map<World.Environment, BigDecimal> pool;

    private final boolean portalCreationAllowed;
    private final double portalCreatePrice;

    private final boolean portalEnterAllowed;
    private final double portalEnterPrice;

    public SettingsRepository(Config config) {
        this.poolEnabled = Map.of(
                World.Environment.NETHER, config.getBoolean("nether.enable"),
                World.Environment.THE_END, config.getBoolean("the-end.enable")
        );

        this.pool = Map.of(
                World.Environment.NETHER, BigDecimal.valueOf(config.getDouble("nether.unlock-amount", 0d)),
                World.Environment.THE_END, BigDecimal.valueOf(config.getDouble("the-end.unlock-amount", 0d))
        );

        this.portalCreationAllowed = config.getBoolean("player-portal-create.enabled");
        this.portalCreatePrice = config.getDouble("player-portal-create.price");

        this.portalEnterAllowed = config.getBoolean("player-portal-enter.enabled");
        this.portalEnterPrice = config.getDouble("player-portal-enter.price");
    }
}
