package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión de la configuración del usuario.
 * <p>
 * Esta clase maneja las solicitudes relacionadas con la página de configuración
 * del usuario, permitiendo ver la información del perfil y cambiar la contraseña.
 * </p>
 */
@Controller
@RequestMapping("/settings")
public class ControllerSettings {

    @Autowired
    private RepositoryUser userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra la página de configuración del usuario.
     * <p>
     * Obtiene el usuario autenticado, lo agrega al modelo y retorna la vista de configuración.
     * </p>
     *
     * @param model El objeto {@link Model} para pasar datos a la vista.
     * @param authentication El objeto {@link Authentication} que representa al usuario autenticado.
     * @return El nombre de la vista de configuración.
     */
    @GetMapping
    public String showSettings(Model model, Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("user", user);
        model.addAttribute("activePage", "settings");
        return "settings";
    }

    /**
     * Procesa la solicitud para cambiar la contraseña del usuario.
     * <p>
     * Este método valida la contraseña actual del usuario, verifica que las contraseñas
     * nuevas coincidan y, si todo es correcto, actualiza la contraseña en la base de datos.
     * Utiliza {@link RedirectAttributes} para mostrar mensajes de éxito o error en la vista.
     * </p>
     *
     * @param currentPassword La contraseña actual del usuario.
     * @param newPassword La nueva contraseña a establecer.
     * @param confirmPassword La confirmación de la nueva contraseña.
     * @param authentication El objeto {@link Authentication} que representa al usuario autenticado.
     * @param ra El objeto {@link RedirectAttributes} para los mensajes flash.
     * @return Una redirección a la página de configuración.
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes ra) {

        Users user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            ra.addFlashAttribute("error", "La contraseña actual es incorrecta");
            return "redirect:/settings";
        }

        // Verificar que las contraseñas nuevas coincidan
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Las contraseñas nuevas no coinciden");
            return "redirect:/settings";
        }

        // Actualizar contraseña
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        ra.addFlashAttribute("success", "Contraseña actualizada correctamente");
        return "redirect:/settings";
    }
}
