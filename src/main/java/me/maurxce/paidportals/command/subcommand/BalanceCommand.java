package me.maurxce.paidportals.command.subcommand;

import md.schorn.spigothelper.command.Context;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.Commands;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.EconomyRepository;

import java.math.BigDecimal;

public class BalanceCommand implements Subcommand {
    private final EconomyRepository economyRepository;

    public BalanceCommand(PaidPortals plugin) {
        this.economyRepository = plugin.getEconomyRepository();
    }

    @Override
    public String execute(Context context) {
        if (!context.hasPermission(Commands.BALANCE.getPermission())) {
            return Language.MISSING_PERMISSION;
        }

        BigDecimal balance = economyRepository.getPoolBalance();

        return Language.POOL_BALANCE
                .replace("{amount}", balance.toString());
    }
}
