package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import it.application.nationaldefencemanagementsystem.Entities.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDto {

    private String username;

    private String email;

    private Role role;

    private Boolean enabled;
}