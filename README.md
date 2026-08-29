# Shyam CoinFlip API

Current release: `v1.0.0`

Add the API dependency, then use `CoinflipProvider` to obtain `CoinflipService` after Shyam CoinFlip has enabled.

## JitPack Dependency

This module is published from the dedicated `Shyam-CoinFlip-API` repository. Request the current release tag from JitPack.

Gradle Kotlin DSL:

```kotlin
repositories {
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.Ravi-DevX")
        }
    }
}

dependencies {
    compileOnly("com.github.Ravi-DevX:Shyam-CoinFlip-API:v1.0.0")
}
```

Do not use `implementation`, relocate, or shade the API into an integration plugin. Shyam-CoinFlip supplies these classes at runtime.

For local development before the first JitPack release:

```kotlin
dependencies {
    compileOnly(files("libs/coinflip-api-1.0.0.jar"))
}
```

Declare the runtime dependency in `plugin.yml` so Bukkit loads CoinFlip first:

```yaml
depend:
  - Shyam-CoinFlip
```

## Obtain The Service

```java
import com.shyam.coinflip.api.v1.CoinflipProvider;
import com.shyam.coinflip.api.v1.CoinflipService;

public final class YourPlugin extends JavaPlugin {
    private CoinflipService coinflip;

    @Override
    public void onEnable() {
        coinflip = CoinflipProvider.require();
    }
}
```

## Cancel A Pending Coinflip

Cancellation performs database work asynchronously. Never block the server thread with `get()` or `join()`. Observe the returned stage and handle the typed result before assuming that a pending wager was removed.

The example below requests a refund and reports the outcome. If the completion handler needs to modify Bukkit state, schedule that work through the appropriate Paper or Folia scheduler.

```java
import com.shyam.coinflip.api.v1.model.CancellationRequest;
import org.bukkit.entity.Player;

public void cancelListing(Player player) {
    coinflip.cancelPendingCoinflip(
            player.getUniqueId(),
            CancellationRequest.refund("administrative-action")
    ).whenComplete((result, unexpectedError) -> {
        if (unexpectedError != null) {
            getLogger().severe("CoinFlip cancellation failed unexpectedly: " + unexpectedError.getMessage());
            return;
        }

        getLogger().info(
                "CoinFlip cancellation for " + player.getUniqueId() +
                        ": " + result.status() +
                        " (operation " + result.operationId() + ")"
        );
    });
}
```

These statuses indicate that no pending listing remains for the requested cancellation:

| Status | Meaning |
| --- | --- |
| `CANCELLED_AND_REFUNDED` | The pending listing was claimed, removed network-wide, and its wager refund was accepted. |
| `CANCELLED_WITHOUT_REFUND` | The caller explicitly requested deletion without a refund. |
| `NO_PENDING_COINFLIP` | No pending listing or active match was found. |

All other statuses indicate that the request did not produce a confirmed terminal cancellation:

| Status | Meaning |
| --- | --- |
| `MATCH_ALREADY_STARTED` | The player is participating in an active roll. |
| `LISTING_ALREADY_CLAIMED` | Another server or player claimed the listing concurrently. |
| `CANCELLED_BY_EVENT` | Another plugin cancelled `CoinflipCancellationRequestEvent`. |
| `ECONOMY_PROVIDER_UNAVAILABLE` | The listing's configured currency provider is unavailable. |
| `REFUND_FAILED` | The provider rejected the refund; CoinFlip restored the listing. |
| `STORAGE_FAILURE` | The database operation could not be completed safely. |
| `SERVICE_UNAVAILABLE` | CoinFlip is disabled or shutting down. |
| `INTERNAL_ERROR` | An unexpected API failure occurred. |

## Reading Listings

The returned models are immutable snapshots. They do not expose live Bukkit inventories or mutable internal match objects.

```java
coinflip.getPendingCoinflip(player.getUniqueId()).ifPresent(listing -> {
    long wager = listing.amount();
    String currency = listing.economyDisplayName();
});

boolean rolling = coinflip.isInActiveCoinflip(player.getUniqueId());
boolean locked = coinflip.isCreationLocked();

coinflip.getActiveCoinflip(player.getUniqueId()).ifPresent(match -> {
    UUID opponent = match.opponentId();
    long startedAt = match.startedAtEpochMillis();
});

coinflip.getCachedStatistics(player.getUniqueId()).ifPresent(stats -> {
    int wins = stats.wins();
    double winRate = stats.winPercentage();
});

Collection<String> economies = coinflip.getAvailableEconomies();
```

## Cancellation Event

`CoinflipCancellationRequestEvent` is fired on the server thread before the database claim. Other plugins may cancel it; doing so leaves the listing and wager unchanged and returns `CANCELLED_BY_EVENT` to the caller.

```java
@EventHandler
public void onApiCancellation(CoinflipCancellationRequestEvent event) {
    getLogger().info(
            "Cancellation requested for " + event.listing().creatorId() +
                    " because of " + event.request().reason()
    );
}
```

## Lifecycle Events

The module provides snapshot-based events that never expose CoinFlip's mutable internal match objects:

- `ListingCreateRequestEvent`: cancellable, before the creator's wager is withdrawn.
- `ListingAcceptRequestEvent`: cancellable, after reservation and before the challenger's wager is withdrawn.
- `CoinflipCancellationRequestEvent`: cancellable, before an API or internal cancellation changes ownership.
- `ListingPublishedEvent`: informational, after a listing enters the local active cache.
- `CoinflipStartedEvent`: informational, after both participants are committed to a match.
- `CoinflipCancellationCompletedEvent`: informational, after an API cancellation succeeds.
- `CoinflipSettledEvent`: informational, after winner selection and payout submission.
- `CoinflipPayoutFailedEvent`: informational, when a currency provider rejects a payout.

`getPendingCoinflip` and `getPendingCoinflips` read the synchronized local cache and do not perform database I/O. `cancelPendingCoinflip` always verifies ownership against the authoritative database.

## Threading And Lifecycle

- The cancellation method may be called from any thread.
- Its returned stage must be observed and must not be blocked.
- Schedule your own Bukkit/player mutations after completion through your plugin's Paper/Folia scheduler.
- Obtain the service during or after `onEnable`, with `Shyam-CoinFlip` declared as a hard dependency.
- Do not retain the service across a full plugin disable/reload cycle.
- Use the result's operation UUID when reporting a cancellation failure.
