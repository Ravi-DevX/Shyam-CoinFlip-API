package com.shyam.coinflip.api.v1;

import com.shyam.coinflip.api.v1.model.CancellationRequest;
import com.shyam.coinflip.api.v1.model.CancellationResult;
import com.shyam.coinflip.api.v1.model.ActiveCoinflip;
import com.shyam.coinflip.api.v1.model.PendingCoinflip;
import com.shyam.coinflip.api.v1.model.PlayerStatistics;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * Stable public API for integrating with Shyam-CoinFlip.
 *
 * <p>Retrieve this service through {@link CoinflipProvider}. Methods returning a
 * {@link CompletionStage} never perform storage I/O on the calling thread.</p>
 */
public interface CoinflipService {

    /** Current public contract revision. */
    String API_VERSION = "1.0.0";

    /**
     * Returns the installed plugin version, for diagnostics only.
     *
     * @return the runtime plugin version
     */
    String getImplementationVersion();

    /**
     * Returns a detached snapshot from this server's synchronized listing cache.
     *
     * @param playerId creator UUID to query
     * @return the pending listing snapshot, if one is cached
     */
    Optional<PendingCoinflip> getPendingCoinflip(UUID playerId);

    /**
     * Returns detached snapshots from this server's synchronized listing cache.
     *
     * @return an immutable collection of current listing snapshots
     */
    Collection<PendingCoinflip> getPendingCoinflips();

    /**
     * Returns whether the player is already participating in a started coinflip.
     *
     * @param playerId player UUID to query
     * @return {@code true} when the player belongs to a started match
     */
    boolean isInActiveCoinflip(UUID playerId);

    /**
     * Returns a detached snapshot of the player's active match on this server.
     *
     * @param playerId participant UUID to query
     * @return active match snapshot, if one is tracked
     */
    Optional<ActiveCoinflip> getActiveCoinflip(UUID playerId);

    /**
     * Returns a detached snapshot of statistics already loaded on this server.
     *
     * @param playerId player UUID to query
     * @return cached statistics, or empty while the profile is not loaded
     */
    Optional<PlayerStatistics> getCachedStatistics(UUID playerId);

    /** @return immutable configured economy identifiers available on this server */
    Collection<String> getAvailableEconomies();

    /**
     * @param economyId configured economy identifier
     * @return whether that economy is available on this server
     */
    boolean isEconomyAvailable(String economyId);

    /**
     * Returns whether creation and joining are administratively locked.
     *
     * @return {@code true} when coinflip mutations are locked
     */
    boolean isCreationLocked();

    /**
     * Atomically claims and cancels a pending listing across the configured network.
     *
     * <p>The returned stage completes with a typed result rather than exceptionally.
     * Callers must wait for completion before performing work that assumes the pending
     * wager has been removed. Bukkit
     * work performed by the caller after completion should still be scheduled through
     * the caller's own Paper/Folia scheduler.</p>
     *
     * @param playerId creator UUID whose pending listing should be cancelled
     * @param request refund policy and audit reason
     * @return a stage containing the terminal, typed cancellation outcome
     */
    CompletionStage<CancellationResult> cancelPendingCoinflip(
            UUID playerId,
            CancellationRequest request
    );

    /**
     * Convenience overload that requests a refund.
     *
     * @param playerId creator UUID whose pending listing should be cancelled
     * @param reason short audit reason supplied by the integration
     * @return a stage containing the terminal, typed cancellation outcome
     */
    default CompletionStage<CancellationResult> cancelPendingCoinflip(UUID playerId, String reason) {
        return cancelPendingCoinflip(playerId, CancellationRequest.refund(reason));
    }
}
