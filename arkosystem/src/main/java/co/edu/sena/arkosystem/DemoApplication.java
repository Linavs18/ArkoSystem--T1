package co.edu.sena.arkosystem;

import co.edu.sena.arkosystem.model.Roles;
import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryRole;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(RepositoryRole repoRole, RepositoryUser repoUser) {
        return args -> {
            // Crear roles si no existen
            if (repoRole.findByName("ROLE_ADMIN").isEmpty()) {
                repoRole.save(new Roles("ROLE_ADMIN", "Administrador del sistema"));
            }
            if (repoRole.findByName("ROLE_USER").isEmpty()) {
                repoRole.save(new Roles("ROLE_USER", "Usuario estándar"));
            }

            // Crear usuario admin si no existe
            if (repoUser.findByEmail("admin@example.com").isEmpty()) {
                Users admin = new Users();
                admin.setName("Administrador");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(repoRole.findByName("ROLE_ADMIN").get());
                repoUser.save(admin);
                System.out.println("Usuario admin creado: admin@example.com / admin123");
            }
        };
    }
}