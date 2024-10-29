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

@Component
@Aspect
@Order(1)
public class RateLimitRequestAspect {
    private final ArrayList<Long> timeRequests = new ArrayList<>();
    @Value("${app.rate.request.limit}")
    private int rateLimit;

    @Value("${app.rate.request.duration}")
    private long rateDuration;

    @Before("@annotation(com.hospital.app.annotations.WithRateLimitRequest)")
    public void rateLimit() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final long timeCurrent = System.currentTimeMillis();
        cleanUpRequestCounts(timeCurrent);
        timeRequests.add(timeCurrent);
        if (timeRequests.size() >= rateLimit) {
            String message = "To many request at endpoint " + requestAttributes.getRequest().getRequestURI() + "! Please try again after " + rateDuration + " milliseconds!";
            throw new RateLimitException(message);
        }
    }

    private void cleanUpRequestCounts(final long timeCurrent) {
        timeRequests.removeIf(time -> isExpired(timeCurrent, time));
    }

    private boolean isExpired(final long timeCurrent, final long timeToCheck) {
        return timeCurrent - timeToCheck > rateDuration;
    }
}
