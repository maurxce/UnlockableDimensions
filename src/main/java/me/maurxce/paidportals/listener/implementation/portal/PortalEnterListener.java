package me.maurxce.paidportals.listener.implementation.portal;

import md.schorn.spigothelper.listener.SimpleListener;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalEnterListener extends SimpleListener {
    private final SettingsRepository settingsRepository;
    private final DimensionRepository dimensionRepository;

    public PortalEnterListener(PaidPortals plugin) {
        this.settingsRepository = plugin.getSettingsRepository();
        this.dimensionRepository = plugin.getDimensionRepository();
    }

    @EventHandler
    public void onPortalCreate(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        World.Environment environment = player.getWorld().getEnvironment();
        boolean allowed = isAllowed(player, environment);
        if (allowed) {
            return;
        }

        player.sendMessage(Language.PORTAL_ENTER_DISABLED);
        event.setCancelled(true);
    }

    private boolean isAllowed(Player player, World.Environment environment) {
        if (player.hasPermission("paidportals.ignore")) return true;

        boolean isEnterAllowed = settingsRepository.isPortalEnterAllowed();
        if (isEnterAllowed) return true;

        return !dimensionRepository.isDimensionLocked(environment);
    }
}
