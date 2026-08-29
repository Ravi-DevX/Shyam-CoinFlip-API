package com.shyam.coinflip.api.v1.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicSnapshotTest {

    @Test
    void activeMatchRecognizesEitherParticipant() {
        UUID creator = UUID.randomUUID();
        UUID opponent = UUID.randomUUID();
        ActiveCoinflip match = new ActiveCoinflip(
                UUID.randomUUID(), creator, "Creator", opponent, "Opponent",
                "VAULT", 5_000L, 100L, 200L
        );

        assertTrue(match.involves(creator));
        assertTrue(match.involves(opponent));
    }

    @Test
    void statisticsExposeDerivedTotals() {
        PlayerStatistics statistics = new PlayerStatistics(
                UUID.randomUUID(), 1, 2, 500L, 1_000L, 3_000L, true
        );

        assertEquals(3, statistics.gamesPlayed());
        assertEquals(33.33D, statistics.winPercentage());
    }
}
