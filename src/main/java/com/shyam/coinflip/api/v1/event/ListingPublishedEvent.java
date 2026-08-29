package com.shyam.coinflip.api.v1.event;

import com.shyam.coinflip.api.v1.model.PendingCoinflip;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

/** Fired after a new listing enters this server's active listing cache. */
public final class ListingPublishedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PendingCoinflip listing;

    /**
     * @param listing immutable snapshot of the published listing
     */
    public ListingPublishedEvent(PendingCoinflip listing) {
        this.listing = Objects.requireNonNull(listing, "listing");
    }

    /**
     * @return immutable snapshot of the published listing
     */
    public PendingCoinflip listing() {
        return listing;
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
