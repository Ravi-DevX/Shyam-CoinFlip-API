package com.shyam.coinflip.api.v1.model;

/**
 * Terminal outcome of an API cancellation request.
 */
public enum CancellationStatus {
    /** Listing removed and refund accepted. */
    CANCELLED_AND_REFUNDED(true),
    /** Listing removed by an explicit no-refund request. */
    CANCELLED_WITHOUT_REFUND(true),
    /** No queued listing or active match was found. */
    NO_PENDING_COINFLIP(true),
    /** The player already belongs to a started match. */
    MATCH_ALREADY_STARTED(false),
    /** Another server or player won the authoritative listing claim. */
    LISTING_ALREADY_CLAIMED(false),
    /** A listener rejected the cancellation request. */
    CANCELLED_BY_EVENT(false),
    /** The listing's economy provider is not available. */
    ECONOMY_PROVIDER_UNAVAILABLE(false),
    /** The refund was rejected and the listing was restored. */
    REFUND_FAILED(false),
    /** Authoritative storage could not complete the operation safely. */
    STORAGE_FAILURE(false),
    /** The runtime API is disabled or shutting down. */
    SERVICE_UNAVAILABLE(false),
    /** An unexpected internal failure stopped the operation. */
    INTERNAL_ERROR(false);

    private final boolean safeToContinue;

    CancellationStatus(boolean safeToContinue) {
        this.safeToContinue = safeToContinue;
    }

    /**
     * Whether the request reached a terminal state with no pending listing left to cancel.
     *
     * @return {@code true} when no pending listing remains for this cancellation request
     */
    public boolean isSafeToContinue() {
        return safeToContinue;
    }
}
