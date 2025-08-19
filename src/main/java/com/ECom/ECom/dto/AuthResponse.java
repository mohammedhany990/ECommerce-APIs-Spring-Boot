package com.ECom.ECom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String fullName;
    private String token;
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant refreshTokenExpiresOn;
}
