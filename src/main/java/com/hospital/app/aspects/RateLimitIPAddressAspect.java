package com.hospital.app.aspects;

import com.hospital.app.annotations.WithRateLimitIPAddress;
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

    @Before("@annotation(withRateLimitIPAddress)")
    public void rateLimit(WithRateLimitIPAddress withRateLimitIPAddress) {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(key, new ArrayList<>());
        requestCounts.get(key).add(currentTime);
        cleanUpRequestCounts(currentTime, withRateLimitIPAddress.duration());
        if (requestCounts.get(key).size() > withRateLimitIPAddress.limit()) {
            String message = "Quá nhiều truy vấn tại  " + requestAttributes.getRequest().getRequestURI() + " từ IP " +
                    key + "! Vui lòng thử lại sau " + withRateLimitIPAddress.duration() + " giây!";
            throw new RateLimitException(message);
        }
    }

    private void cleanUpRequestCounts(final long currentTime, final long rateDuration) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t, rateDuration));
        });
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck, final long rateDuration) {
        return currentTime - timeToCheck > rateDuration;
    }
}
