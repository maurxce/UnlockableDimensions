package me.maurxce.paidportals.command;

import md.schorn.spigothelper.command.Command;
import md.schorn.spigothelper.command.SenderType;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.contract.CommandRouter;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.command.subcommand.BalanceCommand;
import me.maurxce.paidportals.command.subcommand.InfoCommand;
import me.maurxce.paidportals.command.subcommand.PayCommand;

import java.util.HashMap;
import java.util.Map;

@Command(name = "paidportals", permission = "paidportals.use", sender = SenderType.ANY)
public class CommandService extends CommandRouter {
    private final PaidPortals plugin;

    public CommandService(PaidPortals plugin) {
        this.plugin = plugin;
        register(plugin);
    }

    @Override
    protected Map<String, Subcommand> getSubcommands() {
        Map<String, Subcommand> subcommands = new HashMap<>();
        subcommands.put(FALLBACK, new InfoCommand(plugin));
        subcommands.put("pay", new PayCommand(plugin));
        subcommands.put("balance", new BalanceCommand(plugin));

        return subcommands;
    }
}
