package co.edu.sena.arkosystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Proveedor de autenticación usando nuestro UserDetailsServiceImpl
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Aquí se inyecta el UserDetailsImpl
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
<<<<<<< Updated upstream
=======

    // Redirección post-login según rol
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String redirectUrl = "/";

            var authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/admin";
            } else if (authorities.stream().anyMatch(a ->
                    a.getAuthority().equals("ROLE_EMPLOYEE") || a.getAuthority().equals("ROLE_CLIENT"))) {
                redirectUrl = "/dashboard";
            }

            response.sendRedirect(redirectUrl);
        };
    }
>>>>>>> Stashed changes

    // Configuración de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           DaoAuthenticationProvider authProvider) throws Exception {

        http
<<<<<<< Updated upstream
            .authenticationProvider(authProvider)
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos
                .requestMatchers(
                    "/login",
                    "/register",
                    "/forgot-password",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/assets/**",
                    "/webjars/**"
                ).permitAll()
                // Ruta raíz redirige a login
                .requestMatchers("/").permitAll()
                // Rutas específicas por rol
                    .requestMatchers("/dashboard/**").hasAnyRole("CLIENT")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/view/**").hasAnyRole("ADMIN", "EMPLOYEE")
                    .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            )
            .exceptionHandling(exc -> exc
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/error");
                })
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
=======
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(auth -> auth
                        // Recursos públicos
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/forgot-password",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/assets/**",
                                "/webjars/**"
                        ).permitAll()

                        // Rutas específicas por rol
                        .requestMatchers("/dashboard/**").hasAnyRole("CLIENT", "EMPLOYEE")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/view/**").hasAnyRole("ADMIN", "EMPLOYEE") // ✅ Protegido
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email") // ✅ Usa email como usuario
                        .passwordParameter("password")
                        .successHandler(successHandler()) // ✅ Redirección personalizada
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exc -> exc
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/error")
                        )
                );
>>>>>>> Stashed changes

        return http.build();
    }
}
