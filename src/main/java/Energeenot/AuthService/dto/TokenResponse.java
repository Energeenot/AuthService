package Energeenot.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
