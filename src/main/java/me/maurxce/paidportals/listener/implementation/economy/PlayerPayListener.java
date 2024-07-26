package me.maurxce.paidportals.listener.implementation.economy;

import md.schorn.spigothelper.listener.SimpleListener;
import md.schorn.spigothelper.utility.Chat;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.event.PlayerPayEvent;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.repository.DimensionRepository;
import me.maurxce.paidportals.repository.EconomyRepository;
import me.maurxce.paidportals.repository.SettingsRepository;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.math.BigDecimal;

public class PlayerPayListener extends SimpleListener {
    private final SettingsRepository settingsRepository;
    private final EconomyRepository economyRepository;
    private final DimensionRepository dimensionRepository;

    public PlayerPayListener(PaidPortals plugin) {
        this.settingsRepository = plugin.getSettingsRepository();
        this.economyRepository = plugin.getEconomyRepository();
        this.dimensionRepository = plugin.getDimensionRepository();
    }

    @EventHandler
    public void onPayment(PlayerPayEvent event) {
        Player player = event.getPlayer();
        BigDecimal amount = event.getAmount();

        BigDecimal total = economyRepository.addPoolBalance(amount);

        announcePayment(player, amount);
        processUnlock(total);
    }

    private void announcePayment(Player player, BigDecimal amount) {
        String announcement = Language.PAY_ANNOUNCEMENT
                .replace("{username}", player.getName())
                .replace("{amount}", amount.toString());

        Bukkit.broadcastMessage(Chat.translate(announcement));
    }

    private void processUnlock(BigDecimal total) {
        for (World.Environment environment : dimensionRepository.getPermitted()) {
            boolean isPoolEnabled = settingsRepository.getPoolEnabled().containsKey(environment);
            if (!isPoolEnabled) {
                continue;
            }

            boolean isLocked = dimensionRepository.isDimensionLocked(environment);
            if (!isLocked) {
                continue;
            }

            BigDecimal amount = settingsRepository.getPool().getOrDefault(environment, BigDecimal.ZERO);
            if (amount.compareTo(total) > 0) {
                continue;
            }

            dimensionRepository.setDimensionLocked(environment, false);

            String announcement = Language.UNLOCK_ANNOUNCEMENT
                    .replace("{dimension}", environment.toString().replace("_", " "));

            Bukkit.broadcastMessage(Chat.translate(announcement));
        }
    }
}
