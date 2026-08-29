package com.shyam.coinflip.api.v1.model;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Immutable result returned after cancellation processing has finished.
 *
 * @param operationId correlation identifier for logs and support
 * @param status terminal cancellation status
 * @param coinflip listing snapshot when a listing was observed
 * @param refunded whether the economy provider accepted the refund
 */
public record CancellationResult(
        UUID operationId,
        CancellationStatus status,
        Optional<PendingCoinflip> coinflip,
        boolean refunded
) {

    /**
     * Validates a terminal cancellation result.
     *
     * @param operationId correlation identifier for logs and support
     * @param status terminal cancellation status
     * @param coinflip listing snapshot when a listing was observed
     * @param refunded whether the economy provider accepted the refund
     */
    public CancellationResult {
        operationId = Objects.requireNonNull(operationId, "operationId");
        status = Objects.requireNonNull(status, "status");
        coinflip = Objects.requireNonNull(coinflip, "coinflip");
    }

    /**
     * Determines whether this request reached a terminal state with no pending listing
     * left to cancel.
     *
     * @return {@code true} only for statuses that cannot leave hidden escrow behind
     */
    public boolean isSafeToContinue() {
        return status.isSafeToContinue();
    }
}
