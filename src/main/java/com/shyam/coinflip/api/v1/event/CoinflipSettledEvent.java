package com.shyam.coinflip.api.v1.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;
import java.util.UUID;

/**
 * Fired after a completed coinflip has selected its winner and issued the payout.
 */
public final class CoinflipSettledEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID settlementId;
    private final UUID winnerId;
    private final UUID loserId;
    private final String economyId;
    private final long wager;
    private final long payout;
    private final boolean forfeit;
    private final long settledAtEpochMillis;

    /**
     * Creates an immutable settlement notification.
     *
     * @param settlementId unique settlement correlation identifier
     * @param winnerId winning player UUID
     * @param loserId losing player UUID
     * @param economyId stable economy identifier
     * @param wager one participant's original wager
     * @param payout amount submitted to the winner after tax
     * @param forfeit whether disconnect/forfeit policy determined the winner
     * @param settledAtEpochMillis settlement time in Unix epoch milliseconds
     */
    public CoinflipSettledEvent(
            UUID settlementId,
            UUID winnerId,
            UUID loserId,
            String economyId,
            long wager,
            long payout,
            boolean forfeit,
            long settledAtEpochMillis
    ) {
        this.settlementId = Objects.requireNonNull(settlementId, "settlementId");
        this.winnerId = Objects.requireNonNull(winnerId, "winnerId");
        this.loserId = Objects.requireNonNull(loserId, "loserId");
        this.economyId = requireText(economyId, "economyId");
        if (wager < 0L || payout < 0L || settledAtEpochMillis < 0L) {
            throw new IllegalArgumentException("wager, payout and timestamp cannot be negative");
        }
        this.wager = wager;
        this.payout = payout;
        this.forfeit = forfeit;
        this.settledAtEpochMillis = settledAtEpochMillis;
    }

    /** @return unique settlement correlation identifier */
    public UUID settlementId() {
        return settlementId;
    }

    /** @return winning player UUID */
    public UUID winnerId() {
        return winnerId;
    }

    /** @return losing player UUID */
    public UUID loserId() {
        return loserId;
    }

    /** @return stable configured economy identifier */
    public String economyId() {
        return economyId;
    }

    /** @return one participant's original wager */
    public long wager() {
        return wager;
    }

    /** @return amount submitted to the winner after tax */
    public long payout() {
        return payout;
    }

    /** @return whether forfeit policy determined the winner */
    public boolean forfeit() {
        return forfeit;
    }

    /** @return settlement time in Unix epoch milliseconds */
    public long settledAtEpochMillis() {
        return settledAtEpochMillis;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /** @return Bukkit handler list for this event type */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static String requireText(String value, String name) {
        String checked = Objects.requireNonNull(value, name).trim();
        if (checked.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be blank");
        }
        return checked;
    }
}
