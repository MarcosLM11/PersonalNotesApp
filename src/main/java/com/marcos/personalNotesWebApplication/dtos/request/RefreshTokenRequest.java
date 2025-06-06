package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "El refresh token no puede estar vacío")
    private String refreshToken;
}
