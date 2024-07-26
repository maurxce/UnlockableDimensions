package me.maurxce.paidportals.listener;

import md.schorn.spigothelper.listener.SimpleListener;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.listener.implementation.economy.PlayerPayListener;
import me.maurxce.paidportals.listener.implementation.portal.PortalCreateListener;
import me.maurxce.paidportals.listener.implementation.portal.PortalEnterListener;

public class ListenerService {

    public ListenerService(PaidPortals plugin) {
        SimpleListener[] listeners = {
                new PortalCreateListener(plugin),
                new PortalEnterListener(plugin),
                new PlayerPayListener(plugin)
        };

        for (SimpleListener listener : listeners) {
            listener.register(plugin);
        }
    }
}
