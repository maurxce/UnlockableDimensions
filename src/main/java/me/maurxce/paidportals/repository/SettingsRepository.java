package me.maurxce.paidportals.repository;

import lombok.Getter;
import md.schorn.spigothelper.configuration.Config;

@Getter
public class SettingsRepository {
    private final boolean allowPortalCreation;
    private final boolean allowPortalEnter;

    public SettingsRepository(Config config) {
        this.allowPortalCreation = config.getBoolean("player-portal-create");
        this.allowPortalEnter = config.getBoolean("player-portal-enter");
    }
}
