package com.hospital.redis.impls;

import com.hospital.redis.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisLockServiceImpl implements RedisLockService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisLockServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean acquireLock(String lockKey, long lockTimeoutSeconds) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockKey, Duration.ofSeconds(lockTimeoutSeconds));
        return success != null && success;
    }

    @Override
    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}