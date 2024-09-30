package com.hospital.app.jwt;

import com.hospital.app.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtToUserConverter implements
        Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = User.builder()
                .id(Long.valueOf(jwt.getSubject()))
                .build();
        return new UsernamePasswordAuthenticationToken(user,jwt, Collections.emptyList());
    }
}
