package com.hospital.core.events;

import com.hospital.core.entities.reward.Reward;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class UpdateListRewardEvent extends ApplicationEvent {
    private final List<Reward> rewards;

    public UpdateListRewardEvent(Object source, List<Reward> rewards) {
        super(source);
        this.rewards = rewards;
    }
}
