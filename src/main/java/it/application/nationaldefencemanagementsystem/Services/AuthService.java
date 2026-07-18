package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.AuthResponseDto;
import it.application.nationaldefencemanagementsystem.DTOs.LoginDto;
import it.application.nationaldefencemanagementsystem.Entities.User;
import it.application.nationaldefencemanagementsystem.security.JWTTools;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTools jwtTools;

    public AuthService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JWTTools jwtTools
    ) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTools = jwtTools;
    }

    public AuthResponseDto login(LoginDto dto) {

        if (
                dto.getEmail() == null
                        || dto.getEmail().isBlank()
                        || dto.getPassword() == null
                        || dto.getPassword().isBlank()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email e password sono obbligatorie"
            );
        }

        String email =
                dto.getEmail()
                        .trim()
                        .toLowerCase();

        User user =
                userService.findEntityByEmail(email);

        boolean passwordCorrect =
                passwordEncoder.matches(
                        dto.getPassword(),
                        user.getPassword()
                );

        if (
                !passwordCorrect
                        || !user.isEnabled()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email o password non corrette"
            );
        }

        String token =
                jwtTools.generateToken(user);

        return new AuthResponseDto(token);
    }
}