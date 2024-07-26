package me.maurxce.paidportals.repository;

import lombok.Getter;
import me.maurxce.paidportals.database.Database;
import me.maurxce.paidportals.repository.contract.Syncable;
import org.bukkit.World;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DimensionRepository implements Syncable {
    private final Database database;
    private final Map<World.Environment, Boolean> dimensions;
    @Getter private final List<World.Environment> permitted;

    public DimensionRepository(Database database) {
        this.database = database;
        this.dimensions = database.getDimensionStatus();
        this.permitted = List.of(World.Environment.NETHER, World.Environment.THE_END);
    }

    public boolean isDimensionLocked(World.Environment environment) {
        return permitted.contains(environment) && dimensions.getOrDefault(environment, true);
    }

    public boolean allDimensionsUnlocked() {
        if (dimensions.isEmpty()) {
            return false;
        }

        return dimensions.values().stream().noneMatch(Boolean::booleanValue);
    }

    public void setDimensionLocked(World.Environment environment, boolean locked) {
        dimensions.put(environment, locked);
    }

    @Override
    public CompletableFuture<Void> sync() {
        return database.setDimensionStatus(dimensions);
    }
}
