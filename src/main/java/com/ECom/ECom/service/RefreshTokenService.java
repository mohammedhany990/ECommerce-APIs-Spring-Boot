package com.ECom.ECom.service;

import com.ECom.ECom.entity.RefreshToken;
import com.ECom.ECom.entity.User;
import com.ECom.ECom.repository.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

//@Service
//@RequiredArgsConstructor
//public class RefreshTokenService {
//    private final RefreshTokenRepo refreshTokenRepo;
//
//
//
//    public RefreshToken createRefreshToken(User user) {
//               return refreshTokenRepo.save(
//                RefreshToken
//                        .builder()
//                        .user(user)
//                        .token(UUID.randomUUID().toString())
//                        .expiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60))
//                .build());
//    }
//
//    public RefreshToken validateRefreshToken(String token) {
//        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
//                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
//
//        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
//            refreshTokenRepo.delete(refreshToken);
//            throw new RuntimeException("Refresh token expired");
//        }
//
//        return refreshToken;
//    }
//
//    public void deleteByUser(User user) {
//        refreshTokenRepo.deleteByUser(user);
//    }
//}


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    // Create a new refresh token for a user
    public RefreshToken createRefreshToken(User user) {
        return refreshTokenRepo.save(
                RefreshToken
                        .builder()
                        .user(user)
                        .token(UUID.randomUUID().toString())
                        .expiresOn(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                        .isActive(true)
                        .build()
        );
    }

    public RefreshToken getActiveToken(User user) {
        return refreshTokenRepo.findByUserAndIsActiveTrue(user)
                .orElse(null);
    }

    public RefreshToken validateToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiresOn().isBefore(Instant.now()) || !refreshToken.isActive()) {
            refreshToken.setActive(false);
            refreshTokenRepo.save(refreshToken);
            throw new RuntimeException("Refresh token expired or inactive");
        }

        return refreshToken;
    }

    public void deactivateToken(RefreshToken token) {
        token.setActive(false);
        refreshTokenRepo.save(token);
    }
}