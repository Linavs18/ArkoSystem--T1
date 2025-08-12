package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired private RepositoryUser repoUser;
    @Autowired private RepositoryRole repoRole;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Roles role = repoRole.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Rol no existe"));
        user.setRole(role);
        repoUser.save(user);
        return "redirect:/login?registered";
    }
}
