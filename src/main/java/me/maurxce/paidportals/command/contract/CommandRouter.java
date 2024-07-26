package me.maurxce.paidportals.command.contract;

import com.google.common.collect.Iterables;
import md.schorn.spigothelper.command.Context;
import md.schorn.spigothelper.command.SimpleCommand;

import java.util.List;
import java.util.Map;

public abstract class CommandRouter extends SimpleCommand {
    protected static final String FALLBACK = "";
    private final Map<String, Subcommand> subcommands;

    public CommandRouter() {
        this.subcommands = getSubcommands();
    }

    protected abstract Map<String, Subcommand> getSubcommands();

    @Override
    public String execute(Context context) {
        List<String> arguments = context.args();
        String argument = Iterables.get(arguments, 0, FALLBACK);

        String identifier = subcommands.containsKey(argument) ? argument : FALLBACK;
        Subcommand subcommand = subcommands.get(identifier.toLowerCase());

        if (subcommand == null) {
            return "";
        }

        return subcommand.execute(context);
    }
}
