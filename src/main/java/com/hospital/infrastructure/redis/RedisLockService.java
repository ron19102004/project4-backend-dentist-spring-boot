package com.hospital.infrastructure.redis;

public interface RedisLockService {
    boolean acquireLock(String lockKey, long lockTimeoutSeconds);
    void releaseLock(String lockKey);
}
