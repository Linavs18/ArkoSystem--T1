package co.edu.sena.arkosystem.security;

import co.edu.sena.arkosystem.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementación de {@link UserDetails} para Spring Security.
 * <p>
 * Esta clase envuelve un objeto {@link Users} y lo adapta a la interfaz {@link UserDetails},
 * que es el formato que Spring Security requiere para manejar la autenticación y autorización.
 * Proporciona los detalles del usuario, como el nombre de usuario (email), contraseña y roles,
 * para el proceso de autenticación.
 * </p>
 */
public class UserDetailsImpl implements UserDetails {
    private final Users user;

    /**
     * Constructor que inicializa la clase con un objeto de usuario.
     *
     * @param user El objeto {@link Users} que contiene la información del usuario a autenticar.
     */
    public UserDetailsImpl(Users user) {
        this.user = user;
    }

    /**
     * Retorna las autoridades (roles) del usuario.
     * <p>
     * Se crea una autoridad basada en el nombre del rol del usuario. Si el usuario
     * no tiene un rol asignado, se le asigna el rol por defecto "ROLE_USER".
     * </p>
     *
     * @return Una colección de objetos {@link GrantedAuthority} que representan los roles del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = user.getRole() != null ? user.getRole().getName() : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    /**
     * Retorna la contraseña del usuario.
     *
     * @return La contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Retorna el nombre de usuario (en este caso, el email).
     *
     * @return El email del usuario.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     * <p>
     * Por defecto, siempre retorna {@code true} para este sistema.
     * </p>
     *
     * @return {@code true} si la cuenta es válida.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     * <p>
     * Por defecto, siempre retorna {@code true} para este sistema.
     * </p>
     *
     * @return {@code true} si la cuenta no está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario no han expirado.
     * <p>
     * Por defecto, siempre retorna {@code true} para este sistema.
     * </p>
     *
     * @return {@code true} si las credenciales son válidas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     * <p>
     * Por defecto, siempre retorna {@code true} para este sistema.
     * </p>
     *
     * @return {@code true} si el usuario está habilitado.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Retorna el objeto de usuario subyacente.
     *
     * @return El objeto {@link Users} envuelto por esta clase.
     */
    public Users getUser() {
        return user;
    }
}