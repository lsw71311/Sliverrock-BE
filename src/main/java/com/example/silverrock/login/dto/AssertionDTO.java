package com.example.silverrock.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AssertionDTO {
    private JwtResponseDTO.TokenInfo tokenInfo;
    private String msg;
}
