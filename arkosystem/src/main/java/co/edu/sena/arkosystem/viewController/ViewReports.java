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

/**
 * Controlador de Spring para la gestión y visualización de reportes del sistema.
 * <p>
 * Esta clase maneja las solicitudes web para generar diversos reportes,
 * como el inventario con bajo stock y el historial de ventas detallado.
 * Se encarga de inyectar las dependencias de los repositorios
 * necesarios para interactuar con la base de datos y preparar los datos
 * para ser presentados en la vista.
 * </p>
 *
 * @version 1.0
 * @since 2025-08-13
 */
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

    /**
     * Genera y retorna una página de reportes para el sistema.
     * <p>
     * Este método procesa dos tipos de reportes:
     * <ul>
     * <li>Un listado de productos con bajo stock.</li>
     * <li>Un historial detallado de todas las ventas, incluyendo los productos comprados.</li>
     * </ul>
     * La información se añade al {@link Model} para que la vista pueda renderizarla.
     * </p>
     *
     * @param model El {@link Model} de Spring para pasar datos a la vista.
     * @return El nombre de la plantilla de la vista ("ViewsReports/Reports") si la operación es exitosa.
     * En caso de error, retorna la vista de error.
     */
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