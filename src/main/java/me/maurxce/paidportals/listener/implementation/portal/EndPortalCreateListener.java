package me.maurxce.paidportals.listener.implementation.portal;

import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.language.Language;
import me.maurxce.paidportals.listener.implementation.portal.contract.PortalCreateListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EndPortalCreateListener extends PortalCreateListener {

    public EndPortalCreateListener(PaidPortals plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEndPortalCreate(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        if (block == null || block.getType() != Material.END_PORTAL_FRAME || action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        World.Environment environment = World.Environment.THE_END;
        boolean canCreate = allowCreation(player, environment);
        if (canCreate) {
            return;
        }

        player.sendMessage(Language.PORTAL_CREATION_DISABLED);
        event.setCancelled(true);
    }
}
