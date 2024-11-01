package com.hospital.app.aspects;

import com.hospital.app.annotations.WithRateLimitIPAddress;
import com.hospital.app.exception.RateLimitException;
import com.hospital.app.utils.TimeFormatter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
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
        final String ipAddress = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(ipAddress, new ArrayList<>());
        requestCounts.get(ipAddress).add(currentTime);
        cleanUpRequestCounts(currentTime, withRateLimitIPAddress.duration());
        if (requestCounts.get(ipAddress).size() > withRateLimitIPAddress.limit()) {
            long tryOnTime = withRateLimitIPAddress.duration() - (currentTime - requestCounts.get(ipAddress).get(0));
            String message = "Quá nhiều truy vấn tại  " + requestAttributes.getRequest().getRequestURI() + " từ IP " +
                    ipAddress + "! Vui lòng thử lại sau " + TimeFormatter.formatMillisecondsToHMS(tryOnTime);
            throw new RateLimitException(message);
        }
    }

    private void cleanUpRequestCounts(final long currentTime, final long rateDuration) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t, rateDuration));
        });
        List<String> keysToRemove = new ArrayList<>();
        for (String key : requestCounts.keySet()) {
            if (requestCounts.get(key).isEmpty()){
                keysToRemove.add(key);
            }
        }
        keysToRemove.forEach(requestCounts::remove);
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck, final long rateDuration) {
        return currentTime - timeToCheck > rateDuration;
    }
}
