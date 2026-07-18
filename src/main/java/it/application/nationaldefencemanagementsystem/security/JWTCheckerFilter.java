package it.application.nationaldefencemanagementsystem.security;

import it.application.nationaldefencemanagementsystem.Entities.User;
import it.application.nationaldefencemanagementsystem.Services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UserService userService;

    public JWTCheckerFilter(
            JWTTools jwtTools,
            UserService userService
    ) {

        this.jwtTools = jwtTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        //Se il token non è presente, lasciamo proseguire la richiestaSarà SecurityConfig a controllare
         // se l'endpoint è pubblico o protetto.

        if (
                authorizationHeader == null
                        || !authorizationHeader
                        .startsWith("Bearer ")
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        try {

            String token =
                    authorizationHeader.substring(7);

            Integer userId =
                    jwtTools.extractUserId(token);

            User user =
                    userService.findEntityById(userId);

            if (!user.isEnabled()) {

                throw new IllegalStateException(
                        "Utente disabilitato"
                );
            }

            List<SimpleGrantedAuthority> authorities =
                    List.of(
                            new SimpleGrantedAuthority(
                                    user.getRole().name()
                            )
                    );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception exception) {

            SecurityContextHolder.clearContext();

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.setCharacterEncoding(
                    "UTF-8"
            );

            response.getWriter().write(
                    "{\"message\":\"Token non valido o scaduto\"}"
            );

            return;
        }

        filterChain.doFilter(
                request,
                response
        );
    }

    //Questi endpoint non devono essercontrollati dal filtro JWT.

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String path =
                request.getServletPath();

        return path.startsWith("/auth/")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html")
                || path.equals("/error")
                || path.equals("/favicon.ico");
    }
}