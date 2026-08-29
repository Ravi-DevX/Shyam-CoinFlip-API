package com.shyam.coinflip.api.v1.model;

import java.util.Objects;
import java.util.UUID;

/** Immutable snapshot of a coinflip that has already started. */
public record ActiveCoinflip(
        UUID matchId,
        UUID creatorId,
        String creatorName,
        UUID opponentId,
        String opponentName,
        String economyId,
        long wager,
        long createdAtEpochMillis,
        long startedAtEpochMillis
) {
    /**
     * Creates a validated active-match snapshot.
     *
     * @param matchId unique match identifier
     * @param creatorId listing creator
     * @param creatorName creator name captured when the match started
     * @param opponentId accepting player
     * @param opponentName opponent name captured when the match started
     * @param economyId configured economy provider identifier
     * @param wager amount committed by each participant
     * @param createdAtEpochMillis listing creation time in Unix epoch milliseconds
     * @param startedAtEpochMillis match start time in Unix epoch milliseconds
     * @throws NullPointerException if an identifier or name is {@code null}
     * @throws IllegalArgumentException if text is blank or a numeric value is negative
     */
    public ActiveCoinflip {
        Objects.requireNonNull(matchId, "matchId");
        Objects.requireNonNull(creatorId, "creatorId");
        Objects.requireNonNull(opponentId, "opponentId");
        creatorName = requireText(creatorName, "creatorName");
        opponentName = requireText(opponentName, "opponentName");
        economyId = requireText(economyId, "economyId");
        if (wager < 0L || createdAtEpochMillis < 0L || startedAtEpochMillis < 0L) {
            throw new IllegalArgumentException("wager and timestamps cannot be negative");
        }
    }

    /**
     * Checks whether the supplied player is one of the two participants.
     *
     * @param playerId player to test
     * @return {@code true} when the player created or accepted this coinflip
     */
    public boolean involves(UUID playerId) {
        return creatorId.equals(playerId) || opponentId.equals(playerId);
    }

    private static String requireText(String value, String name) {
        String checked = Objects.requireNonNull(value, name).trim();
        if (checked.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be blank");
        }
        return checked;
    }
}
