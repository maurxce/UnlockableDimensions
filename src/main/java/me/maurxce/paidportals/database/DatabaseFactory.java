package me.maurxce.paidportals.database;

import lombok.RequiredArgsConstructor;
import md.schorn.spigothelper.configuration.Config;
import md.schorn.spigothelper.logger.Logger;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.database.implementation.H2;
import me.maurxce.paidportals.database.implementation.MongoDB;
import me.maurxce.paidportals.database.implementation.MySQL;

@RequiredArgsConstructor
public class DatabaseFactory {
    private final PaidPortals plugin;

    public Database setupDatabase() {
        Config config = plugin.getConfig();
        String type = config.getString("database.type", "h2");

        return switch (type.toUpperCase()) {
            case "MYSQL" -> new MySQL(plugin);
            case "MONGODB" -> new MongoDB(plugin);
            case "H2" -> new H2(plugin);
            default -> {
                Logger.severe("Invalid database configuration!");
                plugin.disable();
                yield null;
            }
        };
    }
}
