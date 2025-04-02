package org.pets.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class IpBlockService {

    private static final String BLOCKED_IP_PREFIX = "blocked_ip:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void blockIp(String ip) {
        redisTemplate.opsForValue().set(BLOCKED_IP_PREFIX + ip, "blocked", 1, TimeUnit.HOURS);
    }

    public boolean isBlocked(String ip) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLOCKED_IP_PREFIX + ip));
    }
}