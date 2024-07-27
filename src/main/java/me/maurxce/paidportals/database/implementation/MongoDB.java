package me.maurxce.paidportals.database.implementation;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.RequiredArgsConstructor;
import md.schorn.spigothelper.configuration.Config;
import md.schorn.spigothelper.logger.Logger;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.database.Database;
import me.maurxce.paidportals.database.credentials.Credentials;
import org.bson.Document;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class MongoDB implements Database {
    private final PaidPortals plugin;

    private MongoClient client;
    private MongoCollection<Document> economy;
    private MongoCollection<Document> dimensions;

    @Override
    public Database connect() {
        long start = System.currentTimeMillis();
        Logger.info("Initializing Database...");

        Config config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("database");
        Credentials credentials = Credentials.from(section);

        if (!credentials.isValid()) {
            Logger.severe("Database configuration is invalid!");
            plugin.disable();
            return null;
        }

        this.client = MongoClients.create(credentials.getMongoUrl());

        MongoDatabase database = client.getDatabase(credentials.database());
        this.economy = database.getCollection("economy");
        this.dimensions = database.getCollection("dimensions");

        init(start);
        return this;
    }

    @Override
    public void disconnect() {
        Logger.info("Disconnecting Database...");
        client.close();
    }

    @Override
    public void init(long start) {
        Document document = new Document("NETHER", true)
                .append("THE_END", true);

        Document update = new Document("$set", document);
        UpdateOptions updateOptions = new UpdateOptions().upsert(true);

        dimensions.updateOne(document, update, updateOptions);

        long duration = System.currentTimeMillis() - start;
        Logger.info("Database initialized! (%d ms) ", duration);
    }

    @Override
    public BigDecimal getPoolBalance() {
        Document document = economy.find()
                .filter(Filters.exists("paid"))
                .first();

        if (document != null) {
            double paid = document.getDouble("paid");
            return BigDecimal.valueOf(paid);
        }

        Logger.warning("Unable to get pool balance!");
        return BigDecimal.ZERO;
    }

    @Override
    public CompletableFuture<Void> setPoolBalance(BigDecimal amount) {
        return CompletableFuture.runAsync(() -> {
            Document document = new Document("paid", amount);

            Document update = new Document("$set", document);
            UpdateOptions options = new UpdateOptions().upsert(true);

            economy.updateOne(document, update, options);
        });
    }

    @Override
    public Map<World.Environment, Boolean> getDimensionStatus() {
        Map<World.Environment, Boolean> map = new HashMap<>();

        FindIterable<Document> documents = dimensions.find();
        for (Document document : documents) {
            String name = document.getString("name");
            boolean locked = document.getBoolean("isLocked");

            World.Environment environment = World.Environment.valueOf(name);
            map.put(environment, locked);
        }

        return map;
    }

    @Override
    public CompletableFuture<Void> setDimensionStatus(Map<World.Environment, Boolean> map) {
        return CompletableFuture.runAsync(() -> {
            for (Map.Entry<World.Environment, Boolean> entry : map.entrySet()) {
                World.Environment environment = entry.getKey();
                boolean locked = entry.getValue();

                Document filter = new Document("name", environment.toString());

                Document document = new Document("name", environment.toString())
                        .append("isLocked", locked);

                Document update = new Document("$set", document);
                UpdateOptions options = new UpdateOptions().upsert(true);

                economy.updateOne(filter, update, options);
            }
        });
    }
}
