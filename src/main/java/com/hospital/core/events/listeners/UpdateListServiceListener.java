package com.hospital.core.events.listeners;

import com.hospital.core.events.UpdateListServiceEvent;
import com.hospital.infrastructure.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateListServiceListener {
    @Value("${redis.keys.service-all}")
    private String SERVICE_ALL_KEY;
    private final RedisService redisService;

    @Autowired
    public UpdateListServiceListener(RedisService redisService) {
        this.redisService = redisService;
    }

    @EventListener
    public void onUpdateListServiceEvent(UpdateListServiceEvent event) {
        redisService.setList(SERVICE_ALL_KEY, event.getServices());
    }
}
