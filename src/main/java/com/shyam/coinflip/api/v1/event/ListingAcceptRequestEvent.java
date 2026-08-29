package com.shyam.coinflip.api.v1.event;

import com.shyam.coinflip.api.v1.model.PendingCoinflip;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;
import java.util.UUID;

/**
 * Fired after a listing is reserved but before the challenger's wager is withdrawn.
 */
public final class ListingAcceptRequestEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID challengerId;
    private final PendingCoinflip listing;
    private boolean cancelled;

    /**
     * Creates a listing-acceptance request event.
     *
     * @param challengerId player attempting to accept the listing
     * @param listing detached reserved listing
     */
    public ListingAcceptRequestEvent(UUID challengerId, PendingCoinflip listing) {
        this.challengerId = Objects.requireNonNull(challengerId, "challengerId");
        this.listing = Objects.requireNonNull(listing, "listing");
    }

    /** @return UUID of the challenger requesting acceptance */
    public UUID challengerId() {
        return challengerId;
    }

    /** @return immutable reserved listing */
    public PendingCoinflip listing() {
        return listing;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /** @return Bukkit handler list for this event type */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
