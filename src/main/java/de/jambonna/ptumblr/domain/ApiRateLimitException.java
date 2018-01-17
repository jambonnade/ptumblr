package de.jambonna.ptumblr.domain;

/**
 *
 */
public class ApiRateLimitException extends Exception {
    private final ApiRatelimit rateLimit;
    
    public ApiRateLimitException(ApiRatelimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public ApiRatelimit getRateLimit() {
        return rateLimit;
    }
}
