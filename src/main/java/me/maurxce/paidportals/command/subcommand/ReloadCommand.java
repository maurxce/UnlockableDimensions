package me.maurxce.paidportals.command.subcommand;

import md.schorn.spigothelper.command.Context;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.Commands;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.language.Language;

public class ReloadCommand implements Subcommand {
    private final PaidPortals plugin;

    public ReloadCommand() {
        this.plugin = PaidPortals.getInstance();
    }

    @Override
    public String execute(Context context) {
        if (!context.hasPermission(Commands.RELOAD.getPermission())) {
            return Language.MISSING_PERMISSION;
        }

        context.sendMessage(Language.RELOADING);

        plugin.getConfig().reload();
        plugin.getLang().reload();

        return Language.RELOADED;
    }
}
