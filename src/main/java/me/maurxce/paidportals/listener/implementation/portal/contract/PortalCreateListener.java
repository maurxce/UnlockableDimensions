package me.maurxce.paidportals.listener.implementation.portal.contract;

import md.schorn.spigothelper.listener.SimpleListener;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.dependency.required.VaultHook;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
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

    public boolean process(Player player, World.Environment environment) {
        boolean canCreate = allowCreation(player, environment);
        if (canCreate) {
            return true;
        }

        boolean success = withdrawPrice(player);
        if (success) {
            return true;
        }

        return false;
    }

    public boolean allowCreation(Player player, World.Environment environment) {
        return !poolEnabled.containsKey(environment) ||
                player.hasPermission("paidportals.ignore") ||
                settingsRepository.isPortalCreationAllowed() ||
                !dimensionRepository.isDimensionLocked(environment);
    }

    public boolean withdrawPrice(Player player) {
        double price = settingsRepository.getPortalCreatePrice();
        if (!vaultHook.hasBalance(player, price)) {
            player.sendMessage(Language.INSUFFICIENT_FUNDS);
            return false;
        }

        boolean success = vaultHook.withdraw(player, price);
        if (!success) {
            player.sendMessage(Language.GENERIC_ERROR);
            return false;
        }

        return true;
    }
}
