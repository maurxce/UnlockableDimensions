package me.maurxce.paidportals.command.subcommand;

import md.schorn.spigothelper.command.Context;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.Commands;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.EconomyRepository;
import org.bukkit.Bukkit;

import java.math.BigDecimal;

public class ResetCommand implements Subcommand {
    private final EconomyRepository economyRepository;

    public ResetCommand() {
        this.economyRepository = PaidPortals.getInstance().getEconomyRepository();
    }

    @Override
    public String execute(Context context) {
        if (!context.hasPermission(Commands.RESET.getPermission())) {
            return Language.MISSING_PERMISSION;
        }

        economyRepository.setPoolBalance(BigDecimal.ZERO);

        Bukkit.broadcastMessage(Language.POOL_RESET);
        return null;
    }
}
