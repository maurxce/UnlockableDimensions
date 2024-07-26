package me.maurxce.paidportals.command.subcommand;

import lombok.RequiredArgsConstructor;
import md.schorn.spigothelper.command.Context;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.language.Language;

@RequiredArgsConstructor
public class ReloadCommand implements Subcommand {
    private final PaidPortals plugin;

    @Override
    public String execute(Context context) {
        if (!context.hasPermission("paidportals.reload")) {
            return Language.MISSING_PERMISSION;
        }

        context.sendMessage(Language.RELOADING);

        plugin.getConfig().reload();
        plugin.getLang().reload();

        return Language.RELOADED;
    }
}
