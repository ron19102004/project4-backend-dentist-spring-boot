package com.hospital.infrastructure.redis;

public class RedisLocking {
    private final RedisLockService redisLockService;
    private final String lockKey;
    private final long lockTimeOutSeconds;
    private RedisLocking(String lockKey,long lockTimeOutSeconds, RedisLockService redisLockService) {
        this.lockKey = lockKey;
        this.redisLockService = redisLockService;
        this.lockTimeOutSeconds = lockTimeOutSeconds;
    }
    public static RedisLocking register(String lockKey,long lockTimeOutSeconds, RedisLockService redisLockService) {
        return new RedisLocking(lockKey,lockTimeOutSeconds, redisLockService);
    }

    public <E> E handle(RedisLockingHandler<E> handler) {
        E e;
        //if lockKey not be exists -> true (because acquireLock return true if lockKey setIfAbsent be true )
        if (redisLockService.acquireLock(lockKey, lockTimeOutSeconds)) {
            try {
               e = handler.isCreatedLock();
            } finally {
                redisLockService.releaseLock(lockKey);
            }
        } else {
           e = handler.isExistLock();
        }
        return e;
    }
}
