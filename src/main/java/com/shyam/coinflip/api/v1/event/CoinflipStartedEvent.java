package com.shyam.coinflip.api.v1.event;

import com.shyam.coinflip.api.v1.model.ActiveCoinflip;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

/** Fired after both participants are committed and the match becomes active. */
public final class CoinflipStartedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final ActiveCoinflip match;
    private final boolean hostServer;

    /**
     * @param match immutable snapshot of the active match
     * @param hostServer whether this server owns match resolution and payout
     */
    public CoinflipStartedEvent(ActiveCoinflip match, boolean hostServer) {
        this.match = Objects.requireNonNull(match, "match");
        this.hostServer = hostServer;
    }

    /**
     * @return immutable snapshot of the active match
     */
    public ActiveCoinflip match() {
        return match;
    }

    /**
     * @return {@code true} if this server owns match resolution and payout
     */
    public boolean hostServer() {
        return hostServer;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Bukkit handler-list accessor.
     *
     * @return this event type's handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
