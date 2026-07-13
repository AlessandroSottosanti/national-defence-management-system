package it.application.nationaldefencemanagementsystem.DTOs;

import it.application.nationaldefencemanagementsystem.Entities.Role;

public class UserDto {

    private Integer id;
    private String username;
    private String email;
    private Role role;
    private boolean enabled;
}