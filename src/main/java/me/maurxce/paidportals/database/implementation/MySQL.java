package me.maurxce.paidportals.database.implementation;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import md.schorn.spigothelper.configuration.Config;
import md.schorn.spigothelper.logger.Logger;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.database.credentials.Credentials;
import me.maurxce.paidportals.database.Database;
import me.maurxce.paidportals.database.query.Query;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class MySQL implements Database {
    private final PaidPortals plugin;

    private HikariDataSource dataSource;

    @Override
    public Database connect() {
        long start = System.currentTimeMillis();
        Logger.info("Initializing Database...");

        Config config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("database");
        //OldCredentials credentials = OldCredentials.from(database);
        Credentials credentials = Credentials.from(section).withMySQL();

        if (credentials.isInvalid()) {
            Logger.severe("Database configuration is invalid!");
            plugin.disable();
            return null;
        }

        HikariConfig hikariConfig = credentials.getHikariConfig();
        this.dataSource = new HikariDataSource(hikariConfig);

        if (!dataSource.isRunning()) {
            Logger.severe("DataSource is not running!");
            plugin.disable();
            return null;
        }

        init(start);
        return this;
    }

    @Override
    public void disconnect() {
        Logger.info("Disconnecting Database...");

        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public void init(long start) {
        String[] tables = {
                Query.CREATE_TABLE_ECONOMY,
                Query.CREATE_TABLE_DIMENSIONS,
                Query.INSERT_DIMENSIONS
        };

        try (Connection connection = dataSource.getConnection()) {
            for (String table : tables) {
                PreparedStatement preparedStatement = connection.prepareStatement(table);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException exception) {
            Logger.severe("Unable to initialize Database!");
            exception.printStackTrace();
            plugin.disable();
            return;
        }

        long duration = System.currentTimeMillis() - start;
        Logger.info("Database initialized! (%d ms) ", duration);
    }

    @Override
    public BigDecimal getPoolBalance() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.GET_POOL_BALANCE)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBigDecimal(1);
            }
        } catch (SQLException exception) {
            Logger.warning("Unable to get pool balance!");
            exception.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    @Override
    public CompletableFuture<Void> setPoolBalance(BigDecimal amount) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(Query.SET_POOL_BALANCE)) {
                statement.setBigDecimal(1, amount);
                statement.setBigDecimal(2, amount);
                statement.executeUpdate();
            } catch (SQLException exception) {
                Logger.warning("Unable to set pool balance!");
                exception.printStackTrace();
            }
        });
    }

    @Override
    public Map<World.Environment, Boolean> getDimensionStatus() {
        Map<World.Environment, Boolean> map = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.GET_DIMENSION_STATUS)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString(1);
                boolean locked = resultSet.getBoolean(2);

                World.Environment environment = World.Environment.valueOf(name);
                map.put(environment, locked);
            }
        } catch (SQLException exception) {
            Logger.severe("Unable to get dimension status!");
            exception.printStackTrace();
        }

        return map;
    }

    @Override
    public CompletableFuture<Void> setDimensionStatus(Map<World.Environment, Boolean> map) {
        return CompletableFuture.runAsync(() -> {
            if (map.isEmpty()) return;

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(Query.SET_DIMENSION_STATUS)) {
                for (Map.Entry<World.Environment, Boolean> entry : map.entrySet()) {
                    World.Environment environment = entry.getKey();
                    boolean locked = entry.getValue();

                    statement.setString(1, environment.toString());
                    statement.setBoolean(2, locked);
                    statement.setBoolean(3, locked);
                    statement.addBatch();
                }

                statement.executeBatch();
            }  catch (SQLException exception) {
                Logger.warning("Unable to set dimension status!");
                exception.printStackTrace();
            }
        });
    }
}
