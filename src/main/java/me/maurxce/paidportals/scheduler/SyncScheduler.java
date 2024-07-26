package me.maurxce.paidportals.scheduler;

import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.repository.contract.Syncable;
import org.bukkit.Bukkit;

import java.util.List;

public class SyncScheduler implements Runnable {
    private List<Syncable> syncables;

    public SyncScheduler(PaidPortals plugin) {
        this.syncables = List.of(
                plugin.getEconomyRepository(),
                plugin.getDimensionRepository()
        );

        int syncIntervalMinutes = plugin.getConfig().getInt("sync-interval-minutes", 15);
        long delay = 20L * 60 * syncIntervalMinutes;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, delay);
    }

    @Override
    public void run() {
        for (Syncable syncable : syncables) {
            syncable.sync();
        }
    }
}
