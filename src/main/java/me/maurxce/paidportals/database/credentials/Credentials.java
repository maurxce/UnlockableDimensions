package me.maurxce.paidportals.database.credentials;

import com.zaxxer.hikari.HikariConfig;
import md.schorn.spigothelper.logger.Logger;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.database.Database;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;

public record Credentials(
        String host,
        int port,
        String database,
        String username,
        String password,
        int minIdle,
        int maxPoolSize,
        int idleTimeout) {
    public static final Credentials INVALID = new Credentials(null, 3306, null, null, null, 1, 1, 1);

    public static Credentials from(ConfigurationSection section) {
        if (section == null) return INVALID;

        return new Credentials(
                section.getString("host"),
                section.getInt("port"),
                section.getString("name"),
                section.getString("username"),
                section.getString("password"),
                section.getInt("minimum-idle"),
                section.getInt("maximum-pool-size"),
                section.getInt("idle-timeout")
        );
    }

    public boolean isValid() {
        return !this.equals(INVALID);
    }

    public String getJdbcUrl(Database.HikariType type) {
        return switch (type) {
            case MYSQL -> "jdbc:mysql://" + host + ":" + port + "/" + database;
            case SQLITE -> {
                PaidPortals plugin = PaidPortals.getInstance();
                File file = new File(plugin.getDataFolder(), "data.db");

                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException exception) {
                        Logger.severe("Unable to create file " + file.getAbsolutePath());
                        plugin.disable();
                    }
                }

                yield "jdbc:sqlite:" + database;
            }
        };
    }

    public HikariConfig getHikariConfig(Database.HikariType type) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getJdbcUrl(type));
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("PaidPortals");
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.setIdleTimeout(idleTimeout);

        return config;
    }
}