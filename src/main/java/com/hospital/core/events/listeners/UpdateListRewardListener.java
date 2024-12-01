package com.hospital.core.events.listeners;

import com.hospital.core.events.UpdateListRewardEvent;
import com.hospital.infrastructure.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateListRewardListener {
    @Value("${redis.keys.reward-all}")
    private String REWARD_ALL_KEY;
    private final RedisService redisService;
    @Autowired
    public UpdateListRewardListener(RedisService redisService) {
        this.redisService = redisService;
    }
    @EventListener
    public void handle(UpdateListRewardEvent event) {
        redisService.setList(REWARD_ALL_KEY,event.getRewards());
    }
}
