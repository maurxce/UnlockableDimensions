package me.maurxce.paidportals.listener.implementation.portal;

import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.listener.implementation.portal.contract.PortalCreateListener;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.PortalCreateEvent;

public class NetherPortalCreateListener extends PortalCreateListener {

    public NetherPortalCreateListener(PaidPortals plugin) {
        super(plugin);
    }

    @EventHandler
    public void onNetherPortalCreate(PortalCreateEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getReason() != PortalCreateEvent.CreateReason.FIRE) {
            return;
        }

        World.Environment environment = World.Environment.NETHER;
        boolean canCreate = allowCreation(player, environment);
        if (canCreate) {
            return;
        }

        player.sendMessage(Language.PORTAL_CREATION_DISABLED);
        event.setCancelled(true);
    }
}
