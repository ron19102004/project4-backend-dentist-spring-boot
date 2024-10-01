package com.hospital.app.auth;

import com.hospital.app.jwt.JwtUtils;
import com.hospital.app.dto.LoginDTO;
import com.hospital.app.dto.RegisterDTO;
import com.hospital.app.dto.TokenDTO;
import com.hospital.app.entities.User;
import com.hospital.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Default AuthController -> AuthControllerVer1
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private DaoAuthenticationProvider authenticationProvider;
    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    private JwtAuthenticationProvider jwtRefreshTokenAuthProvider;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO){
        User user = User.builder()
                .username(registerDTO.username())
                .password(registerDTO.password())
                .build();
        userService.createUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user,registerDTO.password(), Collections.emptyList());
        return  ResponseEntity.ok(jwtUtils.createToken(authentication));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationProvider.authenticate(UsernamePasswordAuthenticationToken
                .unauthenticated(loginDTO.username(),loginDTO.password()));
        return  ResponseEntity.ok(jwtUtils.createToken(authentication));
    }
    @PostMapping("/token")
    public ResponseEntity<?>  token(@RequestBody TokenDTO tokenDTO){
        Authentication authentication = jwtRefreshTokenAuthProvider
                .authenticate(new BearerTokenAuthenticationToken(tokenDTO.refreshToken()));
        return ResponseEntity.ok(jwtUtils.createToken(authentication));
    }
}
