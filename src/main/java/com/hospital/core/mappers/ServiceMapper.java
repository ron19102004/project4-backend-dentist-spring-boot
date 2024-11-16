package com.hospital.core.mappers;

import com.hospital.core.dto.service.ServiceCreateRequest;
import com.hospital.core.entities.service.Service;
import com.hospital.infrastructure.utils.Slugify;
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
