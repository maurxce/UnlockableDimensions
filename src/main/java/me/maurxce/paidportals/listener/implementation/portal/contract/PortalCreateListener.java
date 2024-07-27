package me.maurxce.paidportals.listener.implementation.portal.contract;

import md.schorn.spigothelper.listener.SimpleListener;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.dependency.required.VaultHook;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
import org.apache.commons.lang.WordUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class PortalCreateListener extends SimpleListener {
    protected final SettingsRepository settingsRepository;
    protected final Map<World.Environment, Boolean> poolEnabled;
    protected final DimensionRepository dimensionRepository;
    protected final VaultHook vaultHook;

    public PortalCreateListener(PaidPortals plugin) {
        this.settingsRepository = plugin.getSettingsRepository();
        this.poolEnabled = settingsRepository.getPoolEnabled();
        this.dimensionRepository = plugin.getDimensionRepository();
        this.vaultHook = plugin.getVaultHook();
    }

    public boolean allowCreation(Player player, World.Environment environment) {
        boolean shouldIgnore = player.hasPermission("paidportals.ignore");
        if (shouldIgnore) {
            return true;
        }

        boolean isLocked = dimensionRepository.isDimensionLocked(environment);
        if (isLocked) {
            return false;
        }

        boolean isCreationAllowed = settingsRepository.isPortalCreationAllowed();
        if (isCreationAllowed) {
            return true;
        }

        return withdrawPrice(player, environment);
    }

    public boolean withdrawPrice(Player player, World.Environment environment) {
        double price = settingsRepository.getPortalCreatePrice();
        if (price <= 0) {
            return true;
        }

        if (!vaultHook.hasBalance(player, price)) {
            player.sendMessage(Language.INSUFFICIENT_FUNDS);
            return false;
        }

        boolean success = vaultHook.withdraw(player, price);

        String name = environment.toString().replace("_", " ");
        String message = !success ? Language.GENERIC_ERROR : Language.PORTAL_CREATE_PAYMENT
                .replace("{amount}", String.valueOf(price))
                .replace("{dimension}", WordUtils.capitalizeFully(name));

        player.sendMessage(message);
        return success;
    }
}
