package Energeenot.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegistrationResponse {
    private String uuid;
    private String role;
}
