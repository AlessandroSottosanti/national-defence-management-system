package it.application.nationaldefencemanagementsystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


//Mi serve per il token
public class AuthResponseDto {

    private String token;
}