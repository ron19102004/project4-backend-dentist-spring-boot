package com.hospital.redis;

public interface RedisLockingHandler<E>{
    E isCreatedLock();
    E isExistLock();
}