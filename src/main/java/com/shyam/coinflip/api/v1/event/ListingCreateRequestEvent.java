package com.shyam.coinflip.api.v1.event;

import com.shyam.coinflip.api.v1.model.PendingCoinflip;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;
import java.util.UUID;

/**
 * Fired before a wager is withdrawn and a listing is published.
 */
public final class ListingCreateRequestEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID creatorId;
    private final PendingCoinflip listing;
    private boolean cancelled;

    /**
     * Creates a listing-publication request event.
     *
     * @param creatorId player attempting to create the listing
     * @param listing detached proposed listing
     */
    public ListingCreateRequestEvent(UUID creatorId, PendingCoinflip listing) {
        this.creatorId = Objects.requireNonNull(creatorId, "creatorId");
        this.listing = Objects.requireNonNull(listing, "listing");
    }

    /** @return UUID of the creator requesting publication */
    public UUID creatorId() {
        return creatorId;
    }

    /** @return immutable proposed listing */
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
