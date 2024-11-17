package com.hospital.redis;

public interface RedisLockService {
    boolean acquireLock(String lockKey, long lockTimeoutSeconds);
    void releaseLock(String lockKey);
}
