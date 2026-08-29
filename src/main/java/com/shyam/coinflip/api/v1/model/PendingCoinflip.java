package com.shyam.coinflip.api.v1.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Detached, immutable view of a pending coinflip listing.
 *
 * @param creatorId listing creator UUID
 * @param creatorName last known creator name
 * @param economyId stable configured economy identifier
 * @param economyDisplayName configured user-facing economy name
 * @param amount escrowed wager amount
 * @param createdAtEpochMillis listing creation time in Unix epoch milliseconds
 */
public record PendingCoinflip(
        UUID creatorId,
        String creatorName,
        String economyId,
        String economyDisplayName,
        long amount,
        long createdAtEpochMillis
) {

    /**
     * Validates and normalizes a listing snapshot.
     *
     * @param creatorId listing creator UUID
     * @param creatorName last known creator name
     * @param economyId stable configured economy identifier
     * @param economyDisplayName configured user-facing economy name
     * @param amount escrowed wager amount
     * @param createdAtEpochMillis listing creation time in Unix epoch milliseconds
     */
    public PendingCoinflip {
        Objects.requireNonNull(creatorId, "creatorId");
        creatorName = requireText(creatorName, "creatorName");
        economyId = requireText(economyId, "economyId");
        economyDisplayName = requireText(economyDisplayName, "economyDisplayName");
        if (amount < 0L) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        if (createdAtEpochMillis < 0L) {
            throw new IllegalArgumentException("createdAtEpochMillis cannot be negative");
        }
    }

    private static String requireText(String value, String name) {
        String checked = Objects.requireNonNull(value, name).trim();
        if (checked.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be blank");
        }
        return checked;
    }
}
