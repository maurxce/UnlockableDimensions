package me.maurxce.paidportals.listener.implementation.portal;

import md.schorn.spigothelper.listener.SimpleListener;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Map;

public class PortalCreateListener extends SimpleListener {
    private final SettingsRepository settingsRepository;
    private final Map<World.Environment, Boolean> poolEnabled;
    private final DimensionRepository dimensionRepository;

    public PortalCreateListener(PaidPortals plugin) {
        this.settingsRepository = plugin.getSettingsRepository();
        this.poolEnabled = settingsRepository.getPoolEnabled();
        this.dimensionRepository = plugin.getDimensionRepository();
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        World.Environment environment = player.getWorld().getEnvironment();
        if (!poolEnabled.containsKey(environment)) {
            return;
        }

        boolean allowed = isAllowed(player, environment);
        if (allowed) {
            return;
        }

        player.sendMessage(Language.PORTAL_CREATION_DISABLED);
        event.setCancelled(true);
    }

    private boolean isAllowed(Player player, World.Environment environment) {
        if (player.hasPermission("paidportals.ignore")) return true;

        boolean isCreateAllowed = settingsRepository.isPortalCreationAllowed();
        if (isCreateAllowed) return true;

        return !dimensionRepository.isDimensionLocked(environment);
    }
}
