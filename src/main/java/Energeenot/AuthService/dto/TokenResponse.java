package Energeenot.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
