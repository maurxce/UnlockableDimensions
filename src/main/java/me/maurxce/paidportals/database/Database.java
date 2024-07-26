package me.maurxce.paidportals.database;

import org.bukkit.World;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Database {
    enum HikariType {
        MYSQL,
        SQLITE
    }

    Database connect();
    void disconnect();
    void init(long start);

    BigDecimal getPoolBalance();
    CompletableFuture<Void> setPoolBalance(BigDecimal amount);

    Map<World.Environment, Boolean> getDimensionStatus();
    CompletableFuture<Void> setDimensionStatus(Map<World.Environment, Boolean> map);
}
