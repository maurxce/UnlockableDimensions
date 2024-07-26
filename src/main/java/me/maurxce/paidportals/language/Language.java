package me.maurxce.paidportals.language;

import md.schorn.spigothelper.configuration.Config;
import md.schorn.spigothelper.utility.Chat;
import me.maurxce.paidportals.PaidPortals;

public class Language {
    public static final String PREFIX;

    public static final String GENERIC_ERROR;
    public static final String MISSING_PERMISSION;
    public static final String INCORRECT_USAGE;

    public static final String INSUFFICIENT_FUNDS;
    public static final String POOL_CLOSED;

    public static final String PORTAL_CREATION_DISABLED;
    public static final String PORTAL_ENTER_DISABLED;

    public static final String RELOADING;
    public static final String RELOADED;

    public static final String PAY_ANNOUNCEMENT;
    public static final String UNLOCK_ANNOUNCEMENT;

    public static final String POOL_BALANCE;
    public static final String POOL_RESET;

    public static final String PLACEHOLDER_LOCKED;
    public static final String PLACEHOLDER_UNLOCKED;

    static {
        PaidPortals plugin = PaidPortals.getInstance();
        Config config = plugin.getConfig();
        Config lang = plugin.getLang();

        PREFIX = config.getString("prefix", "", Chat::translate);

        GENERIC_ERROR = fetch(lang, "GENERIC_ERROR", "&cAn Error occurred.");
        MISSING_PERMISSION = fetch(lang, "MISSING_PERMISSION", "&cYou don't have permission to do this.");
        INCORRECT_USAGE = fetch(lang, "INCORRECT_USAGE", "&cIncorrect usage.");

        INSUFFICIENT_FUNDS = fetch(lang, "INSUFFICIENT_FUNDS", "&cYou don't have enough money to do this!");
        POOL_CLOSED = fetch(lang, "POOL_CLOSED", "All dimensions have been unlocked already.");

        PORTAL_CREATION_DISABLED = fetch(lang, "PORTAL_CREATION_DISABLED", "&cPortal creation is currently disabled!");
        PORTAL_ENTER_DISABLED = fetch(lang, "PORTAL_ENTER_DISABLED", "&cThis dimension is still locked!");

        RELOADING = fetch(lang, "RELOADING", "&aReloading files...");
        RELOADED = fetch(lang, "RELOADED", "&aReloaded files.");

        PAY_ANNOUNCEMENT = fetch(lang, "PAY_ANNOUNCEMENT", "&l{username} &rhas contributed &l${amount}");
        UNLOCK_ANNOUNCEMENT = fetch(lang, "UNLOCK_ANNOUNCEMENT", "&l{dimension} &rhas been unlocked!");

        POOL_BALANCE = fetch(lang, "POOL_BALANCE", "There are currently &l${amount} &rin the dimensions pool!");
        POOL_RESET = fetch(lang, "POOL_RESET", "The economy pool has been reset!");

        PLACEHOLDER_LOCKED = fetch(lang, "PLACEHOLDER_LOCKED", "&c&lLocked");
        PLACEHOLDER_UNLOCKED = fetch(lang, "PLACEHOLDER_UNLOCKED", "&a&lUnlocked");
    }

    private static String fetch(Config config, String path, String def) {
        String message = config.getString(path, def, Chat::translate);
        return PREFIX + message;
    }
}