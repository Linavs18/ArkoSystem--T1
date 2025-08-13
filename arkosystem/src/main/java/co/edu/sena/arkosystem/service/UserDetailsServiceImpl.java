package co.edu.sena.arkosystem.service;

import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import co.edu.sena.arkosystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = repositoryUser.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

<<<<<<< Updated upstream
        String roleName = user.getRole() != null ? user.getRole().getName() : "ROLE_CLIENT";

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(roleName))
        );
=======
        // ✅ Ahora devolvemos nuestra clase UserDetailsImpl
        return new UserDetailsImpl(user);
>>>>>>> Stashed changes
    }
}
