package com.shyam.coinflip.api.v1.model;

import java.util.Objects;
import java.util.UUID;

/** Immutable snapshot of cached player statistics. */
public record PlayerStatistics(
        UUID playerId,
        int wins,
        int losses,
        long profit,
        long totalLost,
        long totalGambled,
        boolean broadcastsEnabled
) {
    /**
     * Creates a cached statistics snapshot.
     *
     * @param playerId player represented by this snapshot
     * @param wins recorded wins
     * @param losses recorded losses
     * @param profit cumulative winnings tracked by the plugin
     * @param totalLost cumulative losing wagers
     * @param totalGambled cumulative wager volume
     * @param broadcastsEnabled whether this player receives coinflip broadcasts
     * @throws NullPointerException if {@code playerId} is {@code null}
     */
    public PlayerStatistics {
        Objects.requireNonNull(playerId, "playerId");
    }

    /**
     * @return the sum of recorded wins and losses
     */
    public int gamesPlayed() {
        return wins + losses;
    }

    /**
     * @return win percentage rounded to two decimal places, or {@code 0.0} with no wins
     */
    public double winPercentage() {
        int total = gamesPlayed();
        if (total <= 0 || wins <= 0) {
            return 0.0D;
        }
        return Math.round(((wins * 100.0D) / total) * 100.0D) / 100.0D;
    }
}
