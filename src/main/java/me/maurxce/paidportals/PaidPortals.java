package me.maurxce.paidportals;

import lombok.Getter;
import md.schorn.spigothelper.configuration.Config;
import me.maurxce.paidportals.database.Database;
import me.maurxce.paidportals.database.DatabaseFactory;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.EconomyRepository;

@Getter
public final class PaidPortals extends SpigotPlugin {

    public static PaidPortals getInstance() {
        return getPlugin(PaidPortals.class);
    }

    private Config config;
    private Config lang;

    private Database database;

    private EconomyRepository economyRepository;
    private DimensionRepository dimensionRepository;

    @Override
    public void onEnable() {
        this.config = new Config("config.yml", this);
        this.lang = new Config("lang.yml", this);

        this.database = new DatabaseFactory(this)
                .setupDatabase()
                .connect();

        this.economyRepository = new EconomyRepository(database);
        this.dimensionRepository = new DimensionRepository(database);
    }

    @Override
    public void onDisable() {

    }
}
