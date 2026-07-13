package it.application.nationaldefencemanagementsystem.DTOs;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RegisterUserDto {
    private String username;
    private String email;
    private String password;
}
