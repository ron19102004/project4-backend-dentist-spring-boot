package com.hospital.app.jwt;

import com.hospital.app.dto.TokenDTO;
import com.hospital.app.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtils {
    @Autowired
    private JwtEncoder jwtAccessTokenEncoder;
    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    private JwtEncoder jwtRefreshTokenEncoder;

    private String genAccessToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant instant = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(15, ChronoUnit.DAYS))
                .subject(user.getId().toString())
                .build();
        return  jwtAccessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
    private String genRefreshToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant instant = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(30, ChronoUnit.DAYS))
                .subject(user.getId().toString())
                .build();
        return  jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public TokenDTO createToken(Authentication authentication){
        if(!(authentication.getPrincipal() instanceof User user)){
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User type",
                    authentication.getPrincipal().getClass()));
        }
        String refreshToken;
        if(authentication.getCredentials() instanceof Jwt jwt){
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now,expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired < 3){
                refreshToken = genRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = genRefreshToken(authentication);
        }
        String accessToken = genAccessToken(authentication);
        return new TokenDTO(accessToken,refreshToken,user.getId());
    }
}
