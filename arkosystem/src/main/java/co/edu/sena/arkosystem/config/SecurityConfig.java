package co.edu.sena.arkosystem.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * Clase de configuración de seguridad para la aplicación.
 * <p>
 * Esta clase habilita la seguridad web de Spring Security y define la configuración
 * para la autenticación, la autorización y el manejo de los filtros de seguridad.
 * Utiliza un {@link BCryptPasswordEncoder} para codificar contraseñas y un
 * {@link DaoAuthenticationProvider} para autenticar usuarios con un servicio de
 * detalles de usuario.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define el bean para el codificador de contraseñas.
     * <p>
     * Se utiliza {@link BCryptPasswordEncoder} para almacenar de forma segura las contraseñas
     * de los usuarios.
     * </p>
     *
     * @return Una instancia de {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define el bean para el proveedor de autenticación.
     * <p>
     * Este proveedor utiliza el {@link UserDetailsService} y el {@link PasswordEncoder}
     * para autenticar a los usuarios.
     * </p>
     *
     * @param userDetailsService El servicio que carga los detalles del usuario.
     * @param passwordEncoder El codificador de contraseñas.
     * @return Una instancia de {@link DaoAuthenticationProvider}.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Define el bean para el gestor de autenticación.
     * <p>
     * El gestor de autenticación es un componente clave de Spring Security que
     * coordina el proceso de autenticación.
     * </p>
     *
     * @param authConfig La configuración de autenticación.
     * @return Una instancia de {@link AuthenticationManager}.
     * @throws Exception Si ocurre un error al obtener el gestor de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Define el bean para el manejador de éxito de autenticación.
     * <p>
     * Este manejador se activa después de una autenticación exitosa y redirige al usuario
     * a una URL específica basándose en su rol.
     * </p>
     *
     * @return Una instancia de {@link AuthenticationSuccessHandler}.
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();
            String redirectUrl = "/";

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT")
                    || a.getAuthority().equals("ROLE_EMPLOYEE"))) {
                redirectUrl = "/dashboard";
            }

            response.sendRedirect(redirectUrl);
        };
    }

    /**
     * Configura la cadena de filtros de seguridad.
     * <p>
     * Este método configura las reglas de autorización, el manejo de formularios de inicio de sesión,
     * el manejo de excepciones y el proceso de cierre de sesión.
     * </p>
     *
     * @param http El objeto {@link HttpSecurity} para configurar la seguridad web.
     * @param authProvider El proveedor de autenticación.
     * @return Una instancia de {@link SecurityFilterChain}.
     * @throws Exception Si ocurre un error durante la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {
        http
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
                        .requestMatchers("/dashboard/**").hasAnyRole("CLIENT", "EMPLOYEE")
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

        return http.build();
    }
}
