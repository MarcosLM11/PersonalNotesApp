package com.marcos.personalNotesWebApplication.dtos.response;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private String username;
}
