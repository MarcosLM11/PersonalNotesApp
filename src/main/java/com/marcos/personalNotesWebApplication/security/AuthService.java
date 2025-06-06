package com.marcos.personalNotesWebApplication.security;

import com.marcos.personalNotesWebApplication.dtos.request.LoginRequest;
import com.marcos.personalNotesWebApplication.dtos.request.RefreshTokenRequest;
import com.marcos.personalNotesWebApplication.dtos.response.JwtResponse;
import com.marcos.personalNotesWebApplication.entities.RefreshTokenEntity;
import com.marcos.personalNotesWebApplication.exceptions.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createToken(authentication);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(authentication.getName());

        return JwtResponse.builder()
            .tokenType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken.getToken())
            .username(authentication.getName())
            .build();
    }

    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(user -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            null,
                            user.getAuthorities()
                    );

                    String accessToken = tokenProvider.createToken(authentication);

                    return JwtResponse.builder()
                            .tokenType("Bearer")
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .username(user.getUsername())
                            .build();
                })
                .orElseThrow(() -> new JwtAuthenticationException("Refresh token no encontrado en la base de datos"));
    }

    public void logout(String username) {
        refreshTokenService.deleteByUsername(username);
    }
}
