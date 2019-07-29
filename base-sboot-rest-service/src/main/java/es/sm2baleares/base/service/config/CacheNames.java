package es.sm2baleares.base.service.config;

/**
 * Cache Names catalog.
 */
public final class CacheNames {

    // Cache prefix for all cache names
    private static final String CACHE_APP_PREFIX = "base.";

    // Cache for heroes by id

    public static final String HEROES_BY_ID = CACHE_APP_PREFIX + "heroesById";


    public static final String USERS_BY_ID = CACHE_APP_PREFIX + "usersById";


    private CacheNames() {
        // Avoid instantiation
    }
}
