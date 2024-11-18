package com.hospital.core.events;

import com.hospital.core.entities.service.Service;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class UpdateListServiceEvent extends ApplicationEvent {
    private List<Service> services;

    public UpdateListServiceEvent(Object source, List<Service> services) {
        super(source);
        this.services = services;
    }
}
