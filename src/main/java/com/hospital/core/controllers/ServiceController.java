package com.hospital.core.controllers;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.dto.service.HotServiceResponse;
import com.hospital.core.dto.service.ServiceCreateRequest;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.reward.Reward;
import com.hospital.core.entities.service.Service;
import com.hospital.core.events.UpdateListRewardEvent;
import com.hospital.core.events.UpdateListServiceEvent;
import com.hospital.core.services.ServiceService;
import com.hospital.infrastructure.utils.ResponseLayout;
import com.hospital.redis.RedisLockService;
import com.hospital.redis.RedisLocking;
import com.hospital.redis.RedisLockingHandler;
import com.hospital.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/services/v1")
public class ServiceController {
    private final ServiceService serviceService;
    private final RedisService redisService;
    private final ApplicationEventPublisher eventPublisher;
    @Value("${redis.keys.service-all}")
    private String SERVICE_ALL_KEY;
    private final RedisLockService redisLockService;

    @Autowired
    public ServiceController(ServiceService serviceService,
                             RedisService redisService,
                             ApplicationEventPublisher eventPublisher,
                             RedisLockService redisLockService) {
        this.serviceService = serviceService;
        this.redisService = redisService;
        this.eventPublisher = eventPublisher;
        this.redisLockService = redisLockService;
    }

    @GetMapping("/hot")
    @WithRateLimitIPAddress(duration = 15000, limit = 5)
    public ResponseEntity<ResponseLayout<List<HotServiceResponse>>> getHotServices() {
        return ResponseEntity.ok(ResponseLayout
                .<List<HotServiceResponse>>builder()
                .data(this.serviceService.hotServices())
                .success(true)
                .message("Lấy thông tin dịch vụ thành công")
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseLayout<List<Service>>> getAllServices() {
        List<Service> services = RedisLocking
                .register("service:all_lock", 5000, redisLockService)
                .handle(new RedisLockingHandler<>() {
                    private List<Service> services;

                    @Override
                    public List<Service> isCreatedLock() {
                        services = redisService.getList(SERVICE_ALL_KEY);
                        if (services == null) {
                            services = serviceService.getAll();
                            eventPublisher.publishEvent(new UpdateListServiceEvent(this, services));
                        }
                        return services;
                    }

                    @Override
                    public List<Service> isExistLock() {
                        services = redisService.getList(SERVICE_ALL_KEY);
                        if (services == null) {
                            services = Collections.emptyList();
                        }
                        return services;
                    }
                });
        return ResponseEntity.ok(ResponseLayout
                .<List<Service>>builder()
                .data(services)
                .success(true)
                .message("Lấy thông tin dịch vụ thành công")
                .build());
    }

    @GetMapping("/{id}")
    @WithRateLimitIPAddress(duration = 15000, limit = 5)
    public ResponseEntity<ResponseLayout<Service>> getServiceById(@PathVariable("id") Long id) {
        Service service = this.serviceService.getById(id);
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .message(service != null ? "Lấy thông tin dịch vụ thành công" : "Không tìm thấy dịch vụ")
                .success(service != null)
                .data(service)
                .build());
    }

    @HasRole(roles = {Role.ACCOUNTANT})
    @PostMapping("/new")
    public ResponseEntity<ResponseLayout<Service>> createService(@RequestBody ServiceCreateRequest serviceCreateRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .data(this.serviceService.create(serviceCreateRequest))
                .success(true)
                .message("Thêm dịch vụ thành công")
                .build());
    }

    @HasRole(roles = {Role.ACCOUNTANT})
    @DeleteMapping("/{id}")
    @WithRateLimitIPAddress(duration = 10000, limit = 5)
    public ResponseEntity<ResponseLayout<Service>> deleteService(@PathVariable("id") Long id) {
        this.serviceService.delete(id);
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .message("Xóa dịch vụ thành công")
                .success(true)
                .build());
    }
}
