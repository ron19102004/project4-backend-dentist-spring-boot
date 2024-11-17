package com.hospital.core.services.impls;

import com.hospital.core.entities.account.Token;
import com.hospital.core.entities.account.User;
import com.hospital.infrastructure.jwt.JwtCreateTokenDTO;
import com.hospital.core.repositories.TokenRepository;
import com.hospital.core.services.TokenService;
import com.hospital.core.services.UserService;
import com.hospital.infrastructure.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final UserService userService;
    @PersistenceContext
    private EntityManager entityManager;
    private static final int MAX_TOKEN_LOGIN = 5;
    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    @Override
    public boolean validateAccessToken(final String accessToken) {
        Token token = this.tokenRepository.findByAccessTokenOrRefreshToken(accessToken, accessToken);
        return token != null && !token.getExpiredAt().before(VietNamTime.dateNow());
    }

    private void cleanUpTokens(List<Token> tokens) {
        List<Token> expiredTokens = tokens.stream()
                .filter(token -> token.getExpiredAt().before(VietNamTime.dateNow()))
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
    public void saveToken(final JwtCreateTokenDTO jwtCreateTokenDTO, final boolean isMobile) {
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
    public void deleteTokenByAccessToken(final String accessToken) {
        Token token = this.tokenRepository.findByAccessToken(accessToken);
        if (token != null) {
            this.tokenRepository.delete(token);
        }
    }
}
