package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la autenticación y registro de usuarios.
 * <p>
 * Esta clase maneja las solicitudes relacionadas con el inicio de sesión y el
 * registro de nuevos usuarios en la aplicación. Inyecta dependencias para
 * interactuar con los repositorios de usuarios y roles, y el codificador de contraseñas.
 * </p>
 */
@Controller
public class AuthController {
    @Autowired private RepositoryUser repoUser;
    @Autowired private RepositoryRole repoRole;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * Muestra la página de inicio de sesión.
     *
     * @return El nombre de la vista de la página de inicio de sesión.
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * Muestra el formulario de registro de un nuevo usuario.
     * <p>
     * Este método prepara un nuevo objeto {@link Users} para el formulario de registro
     * y lo agrega al modelo para que la vista pueda enlazar los datos.
     * </p>
     *
     * @param model El objeto {@link Model} para pasar datos a la vista.
     * @return El nombre de la vista del formulario de registro.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new Users());
        return "auth/register";
    }

    /**
     * Procesa la solicitud de registro de un nuevo usuario.
     * <p>
     * Este método recibe los datos del formulario, codifica la contraseña del usuario,
     * le asigna el rol por defecto "ROLE_CLIENT" y guarda el nuevo usuario en la base
     * de datos. Finalmente, redirige al usuario a la página de inicio de sesión
     * con un parámetro que indica el registro exitoso.
     * </p>
     *
     * @param user El objeto {@link Users} con los datos del nuevo usuario.
     * @return Una redirección a la página de inicio de sesión.
     */
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Roles role = repoRole.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("Rol no existe"));
        user.setRole(role);
        repoUser.save(user);
        return "redirect:/login?registered";
    }
}
