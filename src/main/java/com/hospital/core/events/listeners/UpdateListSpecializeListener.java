package com.hospital.core.events.listeners;

import com.hospital.core.events.UpdateListSpecializeEvent;
import com.hospital.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateListSpecializeListener {
    @Value("${redis.keys.specialize-all}")
    private String SPECIALIZE_ALL_KEY;
    private final RedisService redisService;
    @Autowired
    public UpdateListSpecializeListener(RedisService redisService) {
        this.redisService = redisService;
    }
    @EventListener
    public void handle(UpdateListSpecializeEvent event) {
        redisService.setList(SPECIALIZE_ALL_KEY,event.getSpecializes());
    }
}
