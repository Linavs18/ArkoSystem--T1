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

@Controller
@RequestMapping("/settings")
public class ControllerSettings {

    @Autowired
    private RepositoryUser userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showSettings(Model model, Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("user", user);
        model.addAttribute("activePage", "settings");
        return "settings";
    }

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
