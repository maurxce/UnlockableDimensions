package me.maurxce.paidportals.dependency.optional;

import md.schorn.spigothelper.utility.Chat;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.dependency.optional.contract.ExtendedHook;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

// paidportals_<dimension>
public class PlaceholderHook extends ExtendedHook {
    private PaidPortals plugin;
    private DimensionRepository dimensionRepository;

    @Override
    public void init() {
        this.plugin = PaidPortals.getInstance();
        this.dimensionRepository = plugin.getDimensionRepository();

        plugin.setPlaceholderHook(this);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "paidportals";
    }

    @Override
    public @NotNull String getAuthor() {
        return "maurxce";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        String name = params.toUpperCase();

        World.Environment environment;
        try {
            environment = World.Environment.valueOf(name);
        } catch (Exception ignored) {
            return null;
        }

        boolean locked = dimensionRepository.isDimensionLocked(environment);
        String response = locked ? Language.PLACEHOLDER_LOCKED : Language.PLACEHOLDER_UNLOCKED;

        return Chat.translate(response);
    }
}
