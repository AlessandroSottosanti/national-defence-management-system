package it.application.nationaldefencemanagementsystem.security;

import it.application.nationaldefencemanagementsystem.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTTools jwtTools,
            UserService userService
    ) throws Exception {

        JWTCheckerFilter jwtCheckerFilter =
                new JWTCheckerFilter(
                        jwtTools,
                        userService
                );

        http
                //Usa le configurazioni CORS già present nel progetto, ad esempio @CrossOrigin.

                .cors(
                        Customizer.withDefaults()
                )

                /*
                 * Applicazione REST con JWT.
                 */
                .csrf(
                        AbstractHttpConfigurer::disable
                )

                .formLogin(
                        AbstractHttpConfigurer::disable
                )

                .httpBasic(
                        AbstractHttpConfigurer::disable
                )

                /*
                 * Non utilizziamo sessioni.
                 */
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )

                .authorizeHttpRequests(
                        requests -> requests

                                .requestMatchers(
                                        "/auth/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/error",
                                        "/favicon.ico"
                                )
                                .permitAll()

                                /*
                                 * GET /users e GET /users/{id}
                                 * sono disponibili solo agli ADMIN.
                                 */
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/users/**"
                                )
                                .hasRole("ADMIN")

                                /*
                                 * POST, PUT e DELETE ereditati
                                 * dall'AbstractController vengono bloccati.
                                 */
                                .requestMatchers(
                                        "/users/**"
                                )
                                .denyAll()

                                /*
                                 * Gli altri endpoint richiedono
                                 * un utente autenticato.
                                 */
                                .anyRequest()
                                .authenticated()
                )

                .exceptionHandling(
                        exceptions -> exceptions

                                /*
                                 * Token assente.
                                 */
                                .authenticationEntryPoint(
                                        (
                                                request,
                                                response,
                                                exception
                                        ) -> {

                                            response.setStatus(
                                                    HttpServletResponse
                                                            .SC_UNAUTHORIZED
                                            );

                                            response.setContentType(
                                                    "application/json"
                                            );

                                            response.setCharacterEncoding(
                                                    "UTF-8"
                                            );

                                            response.getWriter().write(
                                                    "{\"message\":\"Autenticazione richiesta\"}"
                                            );
                                        }
                                )

                                /*
                                 * Utente autenticato,
                                 * ma senza il ruolo richiesto.
                                 */
                                .accessDeniedHandler(
                                        (
                                                request,
                                                response,
                                                exception
                                        ) -> {

                                            response.setStatus(
                                                    HttpServletResponse
                                                            .SC_FORBIDDEN
                                            );

                                            response.setContentType(
                                                    "application/json"
                                            );

                                            response.setCharacterEncoding(
                                                    "UTF-8"
                                            );

                                            response.getWriter().write(
                                                    "{\"message\":\"Non hai i permessi necessari\"}"
                                            );
                                        }
                                )
                )

                /*
                 * Il nostro filtro viene eseguito
                 * prima del filtro standard.
                 */
                .addFilterBefore(
                        jwtCheckerFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /*
     * Utilizzato per cifrare e confrontare
     * le password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}