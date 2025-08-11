package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ControllerDashboard {

    @Autowired
    private RepositoryInventory inventoryRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Inventory> productos = inventoryRepository.findAll();
        model.addAttribute("productos", productos);
        return "dashboard";
    }
}
