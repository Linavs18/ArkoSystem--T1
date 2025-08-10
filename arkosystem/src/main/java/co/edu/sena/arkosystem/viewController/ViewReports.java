package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.repository.RepositoryInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewReports {
    @Autowired
    private RepositoryInventory inventoryRepository;

    @GetMapping("/view/reports")
    public String lowStockReport(Model model) {
        model.addAttribute("lowStockList",
                inventoryRepository.findLowStockItems()); // Consulta: stock <= min_stock
        return "ViewsReports/Reports"; // HTML de la vista
    }
}
