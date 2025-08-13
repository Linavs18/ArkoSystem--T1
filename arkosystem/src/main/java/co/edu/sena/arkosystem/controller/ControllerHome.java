package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryClients;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ControllerHome {
    @Autowired
    private RepositoryInventory inventoryRepository;

    @Autowired
    private RepositoryClients clientsRepository;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si no está autenticado o es usuario anónimo, redirige al login
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }

        // Si está autenticado, redirige según el rol
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))) {
            return "redirect:/dashboard";
        } else if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("activePage", "home");
            List<Inventory> lowStockList = inventoryRepository.findLowStockItems();
            model.addAttribute("lowStockCount", lowStockList.size());
            model.addAttribute("lowStockList", lowStockList);
            model.addAttribute("clientCount", clientsRepository.count());
            model.addAttribute("productCount", inventoryRepository.count());
            return "index";
        } else {
            return "redirect:/employee";
        }
    }
}
