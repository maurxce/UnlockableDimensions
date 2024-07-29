package me.maurxce.paidportals.database.credentials;

import com.zaxxer.hikari.HikariConfig;
import lombok.Builder;
import lombok.Getter;
import me.maurxce.paidportals.PaidPortals;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

@Getter
@Builder(setterPrefix = "with")
public class Credentials {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final int minIdle;
    private final int maxPoolSize;
    private final int idleTimeout;
    private final String driver;
    private final String connectionUrl;

    public static final CredentialsBuilder INVALID = Credentials.builder()
            .withHost(null)
            .withPort(0)
            .withDatabase(null)
            .withUsername(null)
            .withPassword(null)
            .withMinIdle(1)
            .withMaxPoolSize(1)
            .withIdleTimeout(1);

    public static CredentialsBuilder from(ConfigurationSection section) {
        if (section == null) {
            return INVALID;
        }

        return Credentials.builder()
                .withHost(section.getString("host"))
                .withPort(section.getInt("port"))
                .withDatabase(section.getString("name"))
                .withUsername(section.getString("username"))
                .withPassword(section.getString("password"))
                .withMinIdle(section.getInt("minimum-idle"))
                .withMaxPoolSize(section.getInt("maximum-pool-size"))
                .withIdleTimeout(section.getInt("idle-timeout"));
    }

    public boolean isInvalid() {
        return host == null || port == 0 || database == null;
    }

    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(connectionUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("PaidPortals");
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.setIdleTimeout(idleTimeout);

        return config;
    }

    public static class CredentialsBuilder {
        public Credentials withMySQL() {
            this.driver = "com.mysql.jdbc.Driver";
            this.connectionUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;

            return this.build();
        }

        public Credentials withH2() {
            File file = new File(PaidPortals.getInstance().getDataFolder(), "/data/data.db");
            this.driver = "org.h2.Driver";
            this.connectionUrl = "jdbc:h2:file:" + file.getAbsolutePath() + ";MODE=MySQL";

            return this.build();
        }

        public Credentials withMongo() {
            this.connectionUrl = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" +
                    "?authMechanism=SCRAM-SHA-256" + "&maxPoolSize=" + maxPoolSize + "&maxIdleTimeMS=" + idleTimeout;

            return this.build();
        }
    }
}
