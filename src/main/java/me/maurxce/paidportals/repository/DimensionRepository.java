package me.maurxce.paidportals.repository;

import me.maurxce.paidportals.database.Database;
import me.maurxce.paidportals.repository.contract.Syncable;
import org.bukkit.World;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DimensionRepository implements Syncable {
    private final Database database;
    private final Map<World.Environment, Boolean> dimensions;
    private final List<World.Environment> permitted;

    public DimensionRepository(Database database) {
        this.database = database;
        this.dimensions = database.getDimensionStatus();
        this.permitted = List.of(World.Environment.NETHER, World.Environment.THE_END);
    }

    public List<World.Environment> getLockedDimensions() {
        return dimensions.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean isDimensionLocked(World.Environment environment) {
        return permitted.contains(environment) && dimensions.getOrDefault(environment, true);
    }

    public boolean allDimensionsUnlocked() {
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
