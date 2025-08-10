package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryClients;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import org.springframework.beans.factory.annotation.Autowired;
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
        model.addAttribute("activePage", "home");

        List<Inventory> lowStockList = inventoryRepository.findLowStockItems();
        model.addAttribute("lowStockCount", lowStockList.size());
        model.addAttribute("lowStockList", lowStockList);

        long clientCount = clientsRepository.count();
        model.addAttribute("clientCount", clientCount);

        // Productos en inventario
        long productCount = inventoryRepository.count();
        model.addAttribute("productCount", productCount);

        return "index";
    }
}
