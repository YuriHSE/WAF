package org.pets.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter implements RequestFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS = 100;
    private static final int TIME_WINDOW = 1;

    @Override
    public boolean filter(ServerWebExchange exchange) {
        String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
        String key = "rate_limit:" + ip;

        Long requestCount = redisTemplate.opsForValue().increment(key, 1);
        if (requestCount == 1) {
            redisTemplate.expire(key, TIME_WINDOW, TimeUnit.MINUTES);
        }

        if (requestCount > MAX_REQUESTS) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return true;
        }
        return false;
    }
}

