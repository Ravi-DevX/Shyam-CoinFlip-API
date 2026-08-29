package com.shyam.coinflip.api.v1.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;
import java.util.UUID;

/** Fired when a currency adapter rejects the winner payout. */
public final class CoinflipPayoutFailedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID settlementId;
    private final UUID winnerId;
    private final UUID loserId;
    private final String economyId;
    private final long wager;
    private final long attemptedPayout;

    /**
     * Creates a failed-payout notification.
     *
     * @param settlementId unique identifier for correlating logs and recovery work
     * @param winnerId intended payout recipient
     * @param loserId losing participant
     * @param economyId configured economy provider identifier
     * @param wager original amount committed by each participant
     * @param attemptedPayout amount the provider rejected
     */
    public CoinflipPayoutFailedEvent(
            UUID settlementId,
            UUID winnerId,
            UUID loserId,
            String economyId,
            long wager,
            long attemptedPayout
    ) {
        this.settlementId = Objects.requireNonNull(settlementId, "settlementId");
        this.winnerId = Objects.requireNonNull(winnerId, "winnerId");
        this.loserId = Objects.requireNonNull(loserId, "loserId");
        this.economyId = requireText(economyId);
        if (wager < 0L || attemptedPayout < 0L) {
            throw new IllegalArgumentException("wager and payout cannot be negative");
        }
        this.wager = wager;
        this.attemptedPayout = attemptedPayout;
    }

    /** @return unique settlement identifier */
    public UUID settlementId() {
        return settlementId;
    }

    /** @return intended payout recipient */
    public UUID winnerId() {
        return winnerId;
    }

    /** @return losing participant */
    public UUID loserId() {
        return loserId;
    }

    /** @return configured economy provider identifier */
    public String economyId() {
        return economyId;
    }

    /** @return original amount committed by each participant */
    public long wager() {
        return wager;
    }

    /** @return amount the provider rejected */
    public long attemptedPayout() {
        return attemptedPayout;
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

    private static String requireText(String value) {
        String checked = Objects.requireNonNull(value, "economyId").trim();
        if (checked.isEmpty()) {
            throw new IllegalArgumentException("economyId cannot be blank");
        }
        return checked;
    }
}
