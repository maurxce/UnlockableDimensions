package me.maurxce.paidportals.dependency;

import md.schorn.spigothelper.dependency.HookProvider;
import md.schorn.spigothelper.dependency.SimpleHookManager;
import me.maurxce.paidportals.dependency.optional.PlaceholderMiddleware;
import me.maurxce.paidportals.dependency.required.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DependencyService extends SimpleHookManager {

    public DependencyService(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<HookProvider> hooks() {
        return List.of(
                new VaultHook(),
                new PlaceholderMiddleware()
        );
    }
}
