package com.shyam.coinflip.api.v1.event;

import com.shyam.coinflip.api.v1.model.CancellationRequest;
import com.shyam.coinflip.api.v1.model.PendingCoinflip;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;
import java.util.UUID;

/**
 * Fired before a cancellation request changes ownership of a pending listing.
 *
 * <p>Cancelling this event leaves the listing and wager unchanged.</p>
 */
public final class CoinflipCancellationRequestEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID operationId;
    private final PendingCoinflip listing;
    private final CancellationRequest request;
    private boolean cancelled;

    /**
     * Creates a listing-cancellation request event.
     *
     * @param operationId correlation identifier for this cancellation attempt
     * @param listing detached listing targeted by the request
     * @param request refund policy and audit reason
     */
    public CoinflipCancellationRequestEvent(
            UUID operationId,
            PendingCoinflip listing,
            CancellationRequest request
    ) {
        this.operationId = Objects.requireNonNull(operationId, "operationId");
        this.listing = Objects.requireNonNull(listing, "listing");
        this.request = Objects.requireNonNull(request, "request");
    }

    /** @return correlation identifier for this cancellation attempt */
    public UUID operationId() {
        return operationId;
    }

    /** @return immutable listing targeted by the cancellation */
    public PendingCoinflip listing() {
        return listing;
    }

    /** @return refund policy and audit reason */
    public CancellationRequest request() {
        return request;
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
