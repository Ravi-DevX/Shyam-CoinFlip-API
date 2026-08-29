package com.shyam.coinflip.api.v1.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class CancellationApiModelTest {

    @Test
    void validatesAndNormalizesCancellationReason() {
        CancellationRequest request = CancellationRequest.refund("  administrative-action  ");

        assertTrue(request.refund());
        assertEquals("administrative-action", request.reason());
        assertThrows(IllegalArgumentException.class, () -> CancellationRequest.refund(" "));
        assertThrows(IllegalArgumentException.class, () -> CancellationRequest.refund("bad\nreason"));
    }

    @Test
    void onlyTerminalStatusesAreSafeToContinue() {
        assertTrue(CancellationStatus.CANCELLED_AND_REFUNDED.isSafeToContinue());
        assertTrue(CancellationStatus.CANCELLED_WITHOUT_REFUND.isSafeToContinue());
        assertTrue(CancellationStatus.NO_PENDING_COINFLIP.isSafeToContinue());
        assertFalse(CancellationStatus.LISTING_ALREADY_CLAIMED.isSafeToContinue());
        assertFalse(CancellationStatus.REFUND_FAILED.isSafeToContinue());
        assertFalse(CancellationStatus.STORAGE_FAILURE.isSafeToContinue());
    }

    @Test
    void resultUsesOptionalForMissingListing() {
        CancellationResult result = new CancellationResult(
                UUID.randomUUID(),
                CancellationStatus.NO_PENDING_COINFLIP,
                Optional.empty(),
                false
        );

        assertTrue(result.coinflip().isEmpty());
        assertTrue(result.isSafeToContinue());
        assertFalse(result.refunded());
    }

    @Test
    void pendingSnapshotRejectsInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new PendingCoinflip(
                UUID.randomUUID(), "Player", "VAULT", "Money", -1L, 0L
        ));
    }
}
