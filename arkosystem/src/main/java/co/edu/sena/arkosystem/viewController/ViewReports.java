package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ViewReports {

    @Autowired
    private RepositoryInventory inventoryRepository;

    @Autowired
    private RepositorySale saleRepository;

    @Autowired
    private RepositorySaleDetails saleDetailsRepository;

    @Autowired
    private RepositoryInventory productRepository;

    @GetMapping("/view/reports")
    public String generateReports(Model model) {
        try {
            model.addAttribute("lowStockList", inventoryRepository.findLowStockItems());

            List<Sale> sales = saleRepository.findAll();
            List<SaleDetails> allDetails = saleDetailsRepository.findAll();

            Map<Long, List<SaleDetails>> detailsMap = new HashMap<>();
            for (SaleDetails detail : allDetails) {
                Long saleId = detail.getSale().getId();
                detailsMap.computeIfAbsent(saleId, k -> new ArrayList<>()).add(detail);
            }

            for (Sale sale : sales) {
                List<SaleDetails> details = detailsMap.getOrDefault(sale.getId(), new ArrayList<>());
                sale.setSaleDetails(details);

                for (SaleDetails detail : details) {
                    if (detail.getProduct() != null && detail.getProduct().getId() != null) {
                        detail.setProduct(
                                productRepository.findById(detail.getProduct().getId()).orElse(null)
                        );
                    }
                }
            }

            model.addAttribute("sales", sales);

            return "ViewsReports/Reports";

        } catch (Exception e) {
            model.addAttribute("error", "Error al generar reportes: " + e.getMessage());
            return "error";
        }
    }
}