package com.hospital.app.aspects;

import com.hospital.app.annotations.WithRateLimitRequest;
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

    @Before("@annotation(withRateLimitRequest)")
    public void rateLimit(WithRateLimitRequest withRateLimitRequest) {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final long timeCurrent = System.currentTimeMillis();
        cleanUpTimeRequests(timeCurrent, withRateLimitRequest.duration());
        timeRequests.add(timeCurrent);
        if (timeRequests.size() >= withRateLimitRequest.limit()) {
            String message = "Quá nhiều truy vấn tại " + requestAttributes.getRequest().getRequestURI() + "! Vui lòng thử lại sau  " + withRateLimitRequest.duration() + " giây!";
            throw new RateLimitException(message);
        }
    }

    private void cleanUpTimeRequests(final long timeCurrent, final long rateDuration) {
        timeRequests.removeIf(time -> isExpired(timeCurrent, time, rateDuration));
    }

    private boolean isExpired(final long timeCurrent, final long timeToCheck, final long rateDuration) {
        return timeCurrent - timeToCheck > rateDuration;
    }
}
