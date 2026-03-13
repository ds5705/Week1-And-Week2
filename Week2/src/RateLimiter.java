import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    double refillRate;
    long lastRefillTime;

    TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    void refill() {

        long now = System.currentTimeMillis();
        double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
            lastRefillTime = now;
        }
    }

    int remainingTokens() {
        return tokens;
    }
}

public class RateLimiter {

    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int maxRequests = 1000;
    private double refillRate = 1000.0 / 3600.0;

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(maxRequests, refillRate));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.remainingTokens() + " requests remaining)";
        }
        else {
            return "Denied (0 requests remaining)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            return "No data";
        }

        int used = maxRequests - bucket.remainingTokens();

        return "{used: " + used + ", limit: " + maxRequests + "}";
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}