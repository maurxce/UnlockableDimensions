package me.maurxce.paidportals.command.subcommand;

import md.schorn.spigothelper.command.Context;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.Commands;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.dependency.required.VaultHook;
import me.maurxce.paidportals.event.PlayerPayEvent;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;

public class PayCommand implements Subcommand {
    private final DimensionRepository dimensionRepository;
    private final VaultHook vaultHook;

    public PayCommand(PaidPortals plugin) {
        this.dimensionRepository = plugin.getDimensionRepository();
        this.vaultHook = plugin.getVaultHook();
    }

    @Override
    public String execute(Context context) {
        if (!context.isPlayer() ||  context.hasPermission(Commands.PAY.getPermission())) {
            return Language.MISSING_PERMISSION;
        }

        if (dimensionRepository.allDimensionsUnlocked()) {
            return Language.POOL_CLOSED;
        }

        Player player = context.getPlayer();
        List<String> args = context.args();

        if (args.isEmpty()) {
            return Language.INCORRECT_USAGE;
        }

        double amount;
        try {
            amount = Double.parseDouble(args.get(0));
        } catch (NumberFormatException ignore) {
            return Language.INCORRECT_USAGE;
        }

        if (amount <= 0) {
            return Language.INCORRECT_USAGE;
        }

        if (!vaultHook.hasBalance(player, amount)) {
            return Language.INSUFFICIENT_FUNDS;
        }

        boolean success = vaultHook.withdraw(player, amount);
        if (!success) {
            return Language.GENERIC_ERROR;
        }

        PlayerPayEvent event = new PlayerPayEvent(player, BigDecimal.valueOf(amount));
        Bukkit.getPluginManager().callEvent(event);

        return null;
    }
}
