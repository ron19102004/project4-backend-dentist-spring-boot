package com.hospital.app.jwt;

import com.hospital.app.dto.auth.TokenResponse;
import com.hospital.app.entities.account.User;
import com.hospital.app.utils.VietNamTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtils {
    @Autowired
    private JwtEncoder jwtAccessTokenEncoder;
    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    private JwtEncoder jwtRefreshTokenEncoder;

    private Jwt jwtAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Instant instant = VietNamTime.instantNow();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(15, ChronoUnit.DAYS))
                .subject(user.getId().toString())
                .build();
        return jwtAccessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet));
    }

    private Jwt jwtRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Instant instant = VietNamTime.instantNow();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(30, ChronoUnit.DAYS))
                .subject(user.getId().toString())
                .build();
        return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet));
    }

    public JwtCreateTokenDTO createToken(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        String refreshToken = jwtAccessToken(authentication).getTokenValue();
        Jwt jwtAccessToken = jwtRefreshToken(authentication);
        return new JwtCreateTokenDTO(jwtAccessToken, refreshToken, user.getId());
    }

    public JwtCreateTokenDTO createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User type",
                            authentication.getPrincipal().getClass()));
        }
        String refreshToken;
        if (authentication.getCredentials() instanceof Jwt jwt) {
            Instant instant = VietNamTime.instantNow();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(instant, expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired < 3) {
                refreshToken = jwtRefreshToken(authentication).getTokenValue();
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = jwtRefreshToken(authentication).getTokenValue();
        }
        Jwt jwtAccessToken = jwtAccessToken(authentication);
        return new JwtCreateTokenDTO(jwtAccessToken, refreshToken, user.getId());
    }
}
