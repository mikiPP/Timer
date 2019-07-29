package es.sm2baleares.base.service.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Spring cache configuration
 * <p>
 * Cache configuration is loaded from the file cache/spring-cache.xml. This allows to change cache settings just by
 * restarting the application.
 */
@Configuration
@EnableCaching
class SpringServiceCacheConfig {
}