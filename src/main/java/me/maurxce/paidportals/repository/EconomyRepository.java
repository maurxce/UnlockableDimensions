package me.maurxce.paidportals.repository;

import lombok.Getter;
import lombok.Setter;
import me.maurxce.paidportals.database.Database;
import me.maurxce.paidportals.repository.contract.Syncable;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class EconomyRepository implements Syncable {
    private final Database database;
    @Getter @Setter private BigDecimal poolBalance;

    public EconomyRepository(Database database) {
        this.database = database;
        this.poolBalance = database.getPoolBalance();
    }

    public BigDecimal addPoolBalance(BigDecimal balance) {
        this.poolBalance = poolBalance.add(balance);
        return poolBalance;
    }

    @Override
    public CompletableFuture<Void> sync() {
        return database.setPoolBalance(poolBalance);
    }
}
