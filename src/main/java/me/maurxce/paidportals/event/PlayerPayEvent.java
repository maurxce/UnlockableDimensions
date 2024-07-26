package me.maurxce.paidportals.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class PlayerPayEvent extends Event {
    @Getter private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final BigDecimal amount;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
