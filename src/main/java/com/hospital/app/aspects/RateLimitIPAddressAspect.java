package com.hospital.app.aspects;

import com.hospital.app.exception.RateLimitException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
@Order(2)
public class RateLimitIPAddressAspect {
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();
    @Value("${app.rate.ip_address.limit}")
    private int rateLimit;

    @Value("${app.rate.ip_address.duration}")
    private long rateDuration;

    @Before("@annotation(com.hospital.app.annotations.WithRateLimitIPAddress)")
    public void rateLimit() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(key, new ArrayList<>());
        requestCounts.get(key).add(currentTime);
        cleanUpRequestCounts(currentTime);
        if (requestCounts.get(key).size() > rateLimit) {
            String message = "To many request at endpoint " + requestAttributes.getRequest().getRequestURI() + " from IP " +
                    key + "! Please try again after " + rateDuration + " milliseconds!";
            throw new RateLimitException(message);
        }
    }

    private void cleanUpRequestCounts(final long currentTime) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t));
        });
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck) {
        return currentTime - timeToCheck > rateDuration;
    }
}
