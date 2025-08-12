package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/view/sales")
public class ViewSale {

    @Autowired private RepositoryClients clientRepository;
    @Autowired private RepositoryEmployee employeeRepository;
    @Autowired private RepositoryInventory productRepository;
    @Autowired private RepositorySale saleRepository;
    @Autowired private RepositorySaleDetails saleDetailsRepository;

    @GetMapping("/form")
    public String showForm(Model model) {
        try {
            List<Clients> clients = clientRepository.findAll();
            List<Employee> employees = employeeRepository.findAll();
            List<Inventory> products = productRepository.findAll();

            ObjectMapper objectMapper = new ObjectMapper();
            String productsJson = "[]";
            try {
                productsJson = objectMapper.writeValueAsString(products);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            model.addAttribute("clients", clients);
            model.addAttribute("employees", employees);
            model.addAttribute("products", products);
            model.addAttribute("productsJson", productsJson);
            model.addAttribute("activePage", "sales");
            return "ViewSale/SalesForm";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error";
        }
    }
    @PostMapping
    public String createSale(@RequestParam("clientId") Long clientId,
                             @RequestParam("employeeId") Long employeeId,
                             @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam("total") Double total,
                             @RequestParam(value = "productDetails[].productId", required = false) List<Long> productIds,
                             @RequestParam(value = "productDetails[].unitPrice", required = false) List<Double> unitPrices,
                             @RequestParam(value = "productDetails[].quantity", required = false) List<Integer> quantities,
                             RedirectAttributes redirectAttributes) {
        try {
            if (productIds == null || productIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un producto");
                return "redirect:/view/sales/form";
            }


            Sale sale = new Sale();
            Clients client = new Clients();
            client.setId(clientId);
            sale.setClient(client);

            Employee employee = new Employee();
            employee.setId(employeeId);
            sale.setEmployee(employee);

            sale.setPaymentMethod(paymentMethod);
            sale.setTotal(total);
            sale.setStatus("COMPLETED");
            sale.setSaleDate(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

            Sale savedSale = saleRepository.save(sale);

            double calculatedTotal = 0.0;
            for (int i = 0; i < productIds.size(); i++) {
                if (productIds.get(i) != null && unitPrices.get(i) != null && quantities.get(i) != null) {
                    SaleDetails detail = new SaleDetails();
                    detail.setSale(savedSale);

                    Inventory product = new Inventory();
                    product.setId(productIds.get(i));
                    detail.setProduct(product);

                    detail.setUnitPrice(unitPrices.get(i));
                    detail.setQuantity(quantities.get(i));

                    saleDetailsRepository.save(detail);
                    calculatedTotal += unitPrices.get(i) * quantities.get(i);
                }
            }

            savedSale.setTotal(calculatedTotal);
            saleRepository.save(savedSale);

            redirectAttributes.addFlashAttribute("success", "Venta registrada exitosamente");
            return "redirect:/view/sales";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar venta: " + e.getMessage());
            return "redirect:/view/sales/form";
        }
    }

    @GetMapping
    public String listSales(Model model) {
        try {
            model.addAttribute("activePage", "sales");
            return "ViewSale/Sales";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar ventas: " + e.getMessage());
            return "error";
        }
    }
}