package me.maurxce.paidportals.dependency.required;

import md.schorn.spigothelper.dependency.Hook;
import md.schorn.spigothelper.dependency.SimpleHook;
import me.maurxce.paidportals.PaidPortals;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

@Hook("Vault")
public class VaultHook extends SimpleHook {
    private Economy economy;

    @Override
    public void init() {
        PaidPortals plugin = PaidPortals.getInstance();

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return;

        this.economy = rsp.getProvider();
        plugin.setVaultHook(this);
    }

    public boolean hasBalance(Player player, double balance) {
        return economy.has(player, balance);
    }

    public boolean withdraw(Player player, double amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }
}
