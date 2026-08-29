package com.shyam.coinflip.api.v1.event;

import com.shyam.coinflip.api.v1.model.CancellationResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

/** Fired after an API cancellation has reached a successful terminal state. */
public final class CoinflipCancellationCompletedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final CancellationResult result;

    /**
     * @param result successful terminal cancellation result
     * @throws IllegalArgumentException if the result is not safe for the caller to continue
     */
    public CoinflipCancellationCompletedEvent(CancellationResult result) {
        this.result = Objects.requireNonNull(result, "result");
        if (!result.isSafeToContinue()) {
            throw new IllegalArgumentException("result must represent a terminal cancellation");
        }
    }

    /**
     * @return successful terminal cancellation result
     */
    public CancellationResult result() {
        return result;
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
