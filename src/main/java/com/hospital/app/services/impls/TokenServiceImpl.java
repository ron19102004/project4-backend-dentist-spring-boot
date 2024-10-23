package com.hospital.app.services.impls;

import com.hospital.app.entities.account.Token;
import com.hospital.app.entities.account.User;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.jwt.JwtCreateTokenDTO;
import com.hospital.app.repositories.TokenRepository;
import com.hospital.app.services.TokenService;
import com.hospital.app.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserService userService;
    @PersistenceContext
    private EntityManager entityManager;
    private final int MAX_TOKEN_LOGIN = 5;


    @Override
    public void validateAccessToken(String accessToken) {
        Token token = this.tokenRepository.findByAccessTokenOrRefreshToken(accessToken, accessToken);
        if (token == null) {
            throw ServiceException.builder()
                    .clazz(TokenServiceImpl.class)
                    .message("Không tìm thấy token")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (token.getExpiredAt().before(new Date())) {
            throw ServiceException.builder()
                    .clazz(TokenServiceImpl.class)
                    .message("Token đã hết hạn")
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    private void cleanUpTokens(List<Token> tokens) {
        List<Token> expiredTokens = tokens.stream()
                .filter(token -> token.getExpiredAt().before(new Date()))
                .toList();
        if (!expiredTokens.isEmpty()) {
            this.tokenRepository.deleteAll(expiredTokens);
        } else if (tokens.size() >= MAX_TOKEN_LOGIN) {
            Token tokenToDelete = tokens
                    .stream()
                    .filter(token -> !token.getIsMobile())
                    .findFirst()
                    .orElse(tokens
                            .stream()
                            .findFirst()
                            .orElse(null)
                    );
            this.tokenRepository.delete(tokenToDelete);
        }
    }

    @Override
    public void saveToken(JwtCreateTokenDTO jwtCreateTokenDTO, boolean isMobile) {
        List<Token> tokens = this.tokenRepository.findAllByUserIdOrderByIdAsc(jwtCreateTokenDTO.userId());
        cleanUpTokens(tokens);
        User user = this.userService.findById(jwtCreateTokenDTO.userId());
        Token token = Token.builder()
                .user(user)
                .isMobile(isMobile)
                .accessToken(jwtCreateTokenDTO
                        .jwtAccessToken()
                        .getTokenValue())
                .expiredAt(Date.from(Objects.requireNonNull(jwtCreateTokenDTO
                        .jwtAccessToken()
                        .getExpiresAt())))
                .refreshToken(jwtCreateTokenDTO.refreshToken())
                .build();
        this.tokenRepository.save(token);
    }

    @Override
    public void deleteTokenByAccessToken(String accessToken) {
        Token token = this.tokenRepository.findByAccessToken(accessToken);
        if (token != null) {
            this.tokenRepository.delete(token);
        }
    }
}
