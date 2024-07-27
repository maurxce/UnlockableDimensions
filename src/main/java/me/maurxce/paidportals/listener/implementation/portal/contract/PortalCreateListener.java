package me.maurxce.paidportals.listener.implementation.portal.contract;

import md.schorn.spigothelper.listener.SimpleListener;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class PortalCreateListener extends SimpleListener {
    protected final SettingsRepository settingsRepository;
    protected final Map<World.Environment, Boolean> poolEnabled;
    protected final DimensionRepository dimensionRepository;

    public PortalCreateListener(PaidPortals plugin) {
        this.settingsRepository = plugin.getSettingsRepository();
        this.poolEnabled = settingsRepository.getPoolEnabled();
        this.dimensionRepository = plugin.getDimensionRepository();
    }

    public boolean allowCreation(Player player, World.Environment environment) {
        return !poolEnabled.containsKey(environment) ||
                player.hasPermission("paidportals.ignore") ||
                settingsRepository.isPortalCreationAllowed() ||
                !dimensionRepository.isDimensionLocked(environment);
    }
}
