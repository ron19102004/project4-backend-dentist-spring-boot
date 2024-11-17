package com.hospital.core.events;

import com.hospital.core.dto.specialize.SpecializeResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class UpdateListSpecializeEvent extends ApplicationEvent {
    private final List<SpecializeResponse> specializes;

    public UpdateListSpecializeEvent(Object source, List<SpecializeResponse> specializes) {
        super(source);
        this.specializes = specializes;
    }
}
