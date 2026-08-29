package com.shyam.coinflip.api.v1.model;

import java.util.Objects;

/**
 * Options supplied when another plugin cancels a pending listing.
 *
 * @param refund whether the escrowed wager should be returned
 * @param reason nonblank audit reason, limited to 128 characters
 */
public record CancellationRequest(boolean refund, String reason) {

    private static final int MAX_REASON_LENGTH = 128;

    /**
     * Validates and normalizes a cancellation request.
     *
     * @param refund whether the escrowed wager should be returned
     * @param reason nonblank audit reason
     */
    public CancellationRequest {
        reason = Objects.requireNonNull(reason, "reason").trim();
        if (reason.isEmpty()) {
            throw new IllegalArgumentException("reason cannot be blank");
        }
        if (reason.length() > MAX_REASON_LENGTH) {
            throw new IllegalArgumentException("reason cannot exceed " + MAX_REASON_LENGTH + " characters");
        }
        if (reason.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("reason cannot contain control characters");
        }
    }

    /**
     * Creates a cancellation request that returns the wager.
     *
     * @param reason audit reason
     * @return refunding cancellation request
     */
    public static CancellationRequest refund(String reason) {
        return new CancellationRequest(true, reason);
    }

    /**
     * Creates a cancellation request that consumes the wager without returning it.
     *
     * @param reason audit reason
     * @return non-refunding cancellation request
     */
    public static CancellationRequest withoutRefund(String reason) {
        return new CancellationRequest(false, reason);
    }
}
