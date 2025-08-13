package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador para la funcionalidad de recuperación de contraseña.
 * <p>
 * Esta clase maneja las solicitudes GET y POST relacionadas con el proceso
 * de "olvidé mi contraseña", permitiendo a los usuarios restablecer su
 * contraseña si conocen su dirección de correo electrónico.
 * </p>
 */
@Controller
public class ControllerForgotPassword {

    @Autowired
    private RepositoryUser userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra el formulario para restablecer la contraseña.
     * <p>
     * Responde a las solicitudes GET a la URL "/forgot-password".
     * </p>
     *
     * @return El nombre de la vista del formulario de recuperación de contraseña.
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgotPassword";
    }

    /**
     * Procesa la solicitud para restablecer la contraseña.
     * <p>
     * Este método maneja las solicitudes POST desde el formulario de recuperación de contraseña.
     * Valida que las contraseñas coincidan, busca al usuario por su email y, si lo encuentra,
     * actualiza la contraseña con la nueva contraseña codificada. Redirige al usuario a la
     * página de inicio de sesión con un mensaje de éxito o de error.
     * </p>
     *
     * @param email La dirección de correo electrónico del usuario.
     * @param newPassword La nueva contraseña que el usuario desea establecer.
     * @param confirmPassword La confirmación de la nueva contraseña.
     * @param redirectAttributes El objeto {@link RedirectAttributes} para pasar mensajes a la vista redirigida.
     * @return Una redirección a la página de inicio de sesión o de vuelta al formulario con un mensaje de error.
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        try {
            // Validar que las contraseñas coincidan
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/forgot-password";
            }

            // Buscar usuario por email
            Optional<Users> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No existe una cuenta con ese correo electrónico");
                return "redirect:/forgot-password";
            }

            // Actualizar contraseña
            Users user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            return "redirect:/forgot-password";
        }
    }
}