package com.hospital.infrastructure.redis;

public interface RedisLockingHandler<E>{
    E isCreatedLock();
    E isExistLock();
}