package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ControllerDashboard {

    @Autowired
    private RepositoryInventory inventoryRepository;

    @Autowired
    private RepositoryUser repoUser;

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        String email = principal.getName();
        Users usuario = repoUser.findByEmail(email).orElse(null);

        if (usuario != null && (
                usuario.getRole().getName().equals("ROLE_CLIENT") ||
                        usuario.getRole().getName().equals("ROLE_EMPLOYEE")
        )) {
            // Datos del usuario
            model.addAttribute("usuario", usuario);

            // Datos de inventario
            List<Inventory> productos = inventoryRepository.findAll();
            model.addAttribute("productos", productos);

            return "dashboard"; // vista dashboard.html
        }

        return "redirect:/error";
    }
}
