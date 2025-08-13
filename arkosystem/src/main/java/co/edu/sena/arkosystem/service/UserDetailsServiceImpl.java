package co.edu.sena.arkosystem.service;

import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import co.edu.sena.arkosystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de implementación de {@link UserDetailsService} para la autenticación de usuarios.
 * <p>
 * Este servicio es utilizado por Spring Security para cargar los detalles del usuario
 * durante el proceso de autenticación. Se encarga de buscar un usuario por su email
 * en la base de datos y construir un objeto {@link UserDetails} a partir de los datos
 * encontrados.
 * </p>
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    /**
     * Carga un usuario por su email para la autenticación.
     * <p>
     * Este método es una implementación de la interfaz {@link UserDetailsService}.
     * Busca un usuario en la base de datos usando el email proporcionado. Si el usuario
     * es encontrado, se crea y retorna un objeto {@link UserDetailsImpl} con los detalles.
     * Si no se encuentra un usuario con el email, se lanza una {@link UsernameNotFoundException}.
     * </p>
     *
     * @param email El email del usuario que se desea cargar para la autenticación.
     * @return Un objeto {@link UserDetails} que contiene los detalles del usuario autenticado.
     * @throws UsernameNotFoundException si no se encuentra un usuario con el email proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = repositoryUser.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return new UserDetailsImpl(user);
    }
}