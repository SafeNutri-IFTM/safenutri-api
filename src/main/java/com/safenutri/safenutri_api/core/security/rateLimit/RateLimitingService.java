package com.safenutri.safenutri_api.core.security.rateLimit;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import org.springframework.stereotype.Service;

@Service
public class RateLimitingService {

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterAccess(Duration.ofHours(1))
            .build();

    public Bucket resolveBucket(String key) {
        return cache.get(key, this::createNewBucket);
    }

    private Bucket createNewBucket(String key) {
        Refill refill = Refill.intervally(100, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(100, refill);

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
