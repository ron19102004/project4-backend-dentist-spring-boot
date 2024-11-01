package com.hospital.app.jwt;

import com.hospital.app.entities.account.User;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.services.TokenService;
import com.hospital.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * interface Converter<A,B> be used to change class from A to B
 */
@Component
public class JwtToUserConverter implements
        Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) throws ServiceException {
        boolean isTokenValid = this.tokenService.validateAccessToken(jwt.getTokenValue());
        if (!isTokenValid) {
            return new UsernamePasswordAuthenticationToken(null, null);
        }
        User user = this.userService.findById(Long.parseLong(jwt.getSubject()));
        return new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());
    }
}
