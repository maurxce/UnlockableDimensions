package me.maurxce.paidportals.database.credentials;

import com.zaxxer.hikari.HikariConfig;
import org.bukkit.configuration.ConfigurationSection;

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

    public String getJdbcUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public String getMongoUrl() {
        return "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" +
                "?authMechanism=SCRAM-SHA-256" + "&maxPoolSize=" + maxPoolSize + "&maxIdleTimeMS=" + idleTimeout;
    }

    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("PaidPortals");
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.setIdleTimeout(idleTimeout);

        return config;
    }
}
