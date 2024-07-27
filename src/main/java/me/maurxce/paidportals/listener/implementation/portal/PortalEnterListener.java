package me.maurxce.paidportals.listener.implementation.portal;

import md.schorn.spigothelper.listener.SimpleListener;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.dependency.required.VaultHook;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalEnterListener extends SimpleListener {
    private final SettingsRepository settingsRepository;
    private final DimensionRepository dimensionRepository;
    private final VaultHook vaultHook;

    public PortalEnterListener(PaidPortals plugin) {
        this.settingsRepository = plugin.getSettingsRepository();
        this.dimensionRepository = plugin.getDimensionRepository();
        this.vaultHook = plugin.getVaultHook();
    }

    // @TODO needs to get TO environment
    @EventHandler
    public void onPortalEnter(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        Location to = event.getTo();
        if (to == null) {
            return;
        }

        World world = to.getWorld();
        if (world == null) {
            return;
        }

        World.Environment environment = world.getEnvironment();
        boolean allowed = isAllowed(player, environment);
        if (allowed) {
            return;
        }

        boolean success = withdrawPrice(player, environment);
        if (success) {
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

    private boolean withdrawPrice(Player player, World.Environment environment) {
        double price = settingsRepository.getPortalEnterPrice();
        if (price <= 0) {
            return true;
        }

        if (!vaultHook.hasBalance(player, price)) {
            player.sendMessage(Language.INSUFFICIENT_FUNDS);
            return false;
        }

        boolean success = vaultHook.withdraw(player, price);

        String name = environment.toString().replace("_", " ");
        String message = Language.PORTAL_CREATE_PAYMENT
                .replace("{amount}", String.valueOf(price))
                .replace("{dimension}", WordUtils.capitalizeFully(name));

        player.sendMessage(message);
        return success;
    }
}
