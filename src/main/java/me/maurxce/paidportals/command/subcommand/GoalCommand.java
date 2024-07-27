package me.maurxce.paidportals.command.subcommand;

import md.schorn.spigothelper.command.Context;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.Commands;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.language.Language;
import org.apache.commons.lang.WordUtils;
import org.bukkit.World;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class GoalCommand implements Subcommand {
    private final Map<World. Environment, BigDecimal> pool;
    private final List<World. Environment> permitted;

    public GoalCommand() {
        PaidPortals plugin = PaidPortals.getInstance();
        this.pool = plugin.getSettingsRepository().getPool();
        this.permitted = plugin.getDimensionRepository().getPermitted();
    }

    @Override
    public String execute(Context context) {
        if (!context.hasPermission(Commands.GOAL.getPermission())) {
            return Language.MISSING_PERMISSION;
        }

        for (World.Environment environment : permitted) {
            String name = environment.toString().replace("_", " ");
            BigDecimal amount = pool.getOrDefault(environment, BigDecimal.ZERO);

            String message = Language.POOL_GOAL
                    .replace("{dimension}", WordUtils.capitalizeFully(name))
                    .replace("{amount}", amount.toString());

            context.sendMessage(message);
        }

        return null;
    }
}
