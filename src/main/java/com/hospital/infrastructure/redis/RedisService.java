package com.hospital.infrastructure.redis;

import com.hospital.core.entities.reward.Reward;

import java.util.List;

public interface RedisService {
    <E> List<E> getList(String key);

    <E> void setList(String key, List<E> rewards);
}
