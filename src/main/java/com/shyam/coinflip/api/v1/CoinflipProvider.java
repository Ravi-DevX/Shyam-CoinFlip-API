package com.shyam.coinflip.api.v1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;

/**
 * Entry point for obtaining the installed CoinFlip API service.
 */
public final class CoinflipProvider {

    private CoinflipProvider() {
    }

    /**
     * Looks up the currently registered CoinFlip service.
     *
     * @return the service, or an empty optional when the runtime plugin is unavailable
     */
    public static Optional<CoinflipService> get() {
        RegisteredServiceProvider<CoinflipService> registration = Bukkit.getServicesManager()
                .getRegistration(CoinflipService.class);
        return registration == null ? Optional.empty() : Optional.of(registration.getProvider());
    }

    /**
     * Returns the registered service or fails with an actionable dependency message.
     *
     * @return the registered CoinFlip service
     * @throws IllegalStateException when Shyam-CoinFlip is not enabled
     */
    public static CoinflipService require() {
        return get().orElseThrow(() -> new IllegalStateException(
                "Shyam-CoinFlip is not enabled or its API service is unavailable. " +
                        "Add Shyam-CoinFlip to depend in plugin.yml."
        ));
    }
}
