package me.maurxce.paidportals.command;

import md.schorn.spigothelper.command.Command;
import md.schorn.spigothelper.command.Context;
import md.schorn.spigothelper.command.SenderType;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.contract.CommandRouter;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.command.subcommand.*;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "paidportals", permission = "paidportals.use", sender = SenderType.ANY)
public class CommandService extends CommandRouter {

    public CommandService(PaidPortals plugin) {
        register(plugin);
    }

    @Override
    protected Map<String, Subcommand> getSubcommands() {
        Map<String, Subcommand> subcommands = new HashMap<>();
        subcommands.put(FALLBACK, new InfoCommand());
        subcommands.put("pay", new PayCommand());
        subcommands.put("balance", new BalanceCommand());
        subcommands.put("reload", new ReloadCommand());
        subcommands.put("reset", new ResetCommand());

        return subcommands;
    }

    @Override
    public List<String> tab(Context context) {
        List<String> args = context.args();
        List<String> completions = new ArrayList<>();
        int size = args.size();

        List<String> suggestions = new ArrayList<>();
        for (Commands command : Commands.values()) {
            if (!context.hasPermission(command.getPermission())) {
                continue;
            }

            String[] arguments = command.getArguments();
            if (arguments.length < size) {
                continue;
            }

            String argument = arguments[size - 1];
            suggestions.add(argument);
        }

        StringUtil.copyPartialMatches(args.get(size - 1), suggestions, completions);
        return completions;
    }
}
