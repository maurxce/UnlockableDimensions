package me.maurxce.paidportals;

import lombok.Getter;
import lombok.Setter;
import md.schorn.spigothelper.configuration.Config;
import md.schorn.spigothelper.logger.Logger;
import me.maurxce.paidportals.database.Database;
import me.maurxce.paidportals.database.DatabaseFactory;
import me.maurxce.paidportals.dependency.DependencyService;
import me.maurxce.paidportals.dependency.optional.PlaceholderHook;
import me.maurxce.paidportals.dependency.required.VaultHook;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.EconomyRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
import me.maurxce.paidportals.scheduler.SyncScheduler;

@Getter
public final class PaidPortals extends SpigotPlugin {

    public static PaidPortals getInstance() {
        return getPlugin(PaidPortals.class);
    }

    private Config config;
    private Config lang;

    private Database database;

    private SettingsRepository settingsRepository;
    private EconomyRepository economyRepository;
    private DimensionRepository dimensionRepository;

    @Setter private VaultHook vaultHook;
    @Setter private PlaceholderHook placeholderHook;

    @Override
    public void onEnable() {
        this.config = new Config("config.yml", this);
        this.lang = new Config("lang.yml", this);

        Logger.setPrefix(Language.PREFIX);

        this.database = new DatabaseFactory(this)
                .setupDatabase()
                .connect();

        this.settingsRepository = new SettingsRepository(config);
        this.economyRepository = new EconomyRepository(database);
        this.dimensionRepository = new DimensionRepository(database);

        new DependencyService(this);
        new SyncScheduler(this);
    }

    @Override
    public void onDisable() {
        if (database == null) {
            return;
        }

        economyRepository.sync().join();
        dimensionRepository.sync().join();
        database.disconnect();
    }
}
