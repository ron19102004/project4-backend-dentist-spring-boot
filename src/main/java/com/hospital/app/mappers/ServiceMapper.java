package com.hospital.app.mappers;

import com.hospital.app.dto.service.ServiceCreateRequest;
import com.hospital.app.entities.service.Service;
import com.hospital.app.utils.Slugify;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceMapper {
    public Service toService(ServiceCreateRequest serviceCreateRequest) {
        return Service.builder()
                .name(serviceCreateRequest.name())
                .slug(Slugify.toSlug(serviceCreateRequest.name()))
                .description(serviceCreateRequest.description())
                .price(serviceCreateRequest.price())
                .pointReward(serviceCreateRequest.pointReward())
                .build();
    }
}
