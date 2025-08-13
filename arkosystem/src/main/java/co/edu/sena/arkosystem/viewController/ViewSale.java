package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import co.edu.sena.arkosystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime; // <-- ¡Nuevo import!
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view")
public class ViewSale {

    @Autowired private RepositorySale saleRepository;
    @Autowired private RepositorySaleDetails saleDetailsRepository;
    @Autowired private RepositoryInventory productRepository;
    @Autowired private RepositoryClients clientsRepository;
    @Autowired private RepositoryUser userRepository;
    @Autowired private RepositoryEmployee employeeRepository;

    @GetMapping("/sales")
    public String listSales(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("activePage", "sales");

        if (userDetails != null) {
            model.addAttribute("currentUsername", userDetails.getUsername());
        }

        try {
            List<Clients> allClients = clientsRepository.findAll();
            List<Inventory> allProducts = productRepository.findAll();

            List<String> clientNames = allClients.stream().map(Clients::getName).collect(Collectors.toList());
            List<Inventory> availableProducts = allProducts.stream().filter(p -> p.getAvailableQuantity() > 0).collect(Collectors.toList());

            model.addAttribute("clientNames", clientNames);
            model.addAttribute("products", availableProducts);

            return "ViewSale/Sales";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar datos: " + e.getMessage());
            model.addAttribute("clientNames", new ArrayList<>());
            model.addAttribute("products", new ArrayList<>());
            return "ViewSale/Sales";
        }
    }

    @PostMapping("/process-sale")
    @ResponseBody
    public ResponseEntity<String> processSale(@RequestBody Map<String, Object> saleData, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            String clientName = (String) saleData.get("client");
            String paymentMethodStr = (String) saleData.get("paymentMethod");
            List<Map<String, Object>> items = (List<Map<String, Object>>) saleData.get("items");
            double total = Double.parseDouble(saleData.get("total").toString());

            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body("No hay productos en la venta.");
            }
            if (clientName == null || clientName.isEmpty()) {
                return ResponseEntity.badRequest().body("Debe seleccionar un cliente.");
            }

            Clients client = clientsRepository.findByName(clientName)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clientName));

            Users user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userDetails.getUsername()));

            Employee employee = employeeRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado para el usuario: " + user.getId()));

            Sale newSale = new Sale();
            newSale.setClient(client);
            newSale.setSaleDate(LocalDateTime.now()); // <-- ¡Corrección aquí!
            newSale.setTotal(total);
            newSale.setPaymentMethod(paymentMethodStr);
            newSale.setEmployee(employee);
            newSale.setStatus("COMPLETED");

            Sale savedSale = saleRepository.save(newSale);

            for (Map<String, Object> item : items) {
                Long productId = Long.valueOf(item.get("id").toString());
                int quantity = Integer.parseInt(item.get("quantity").toString());

                Inventory product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                if (product.getAvailableQuantity() < quantity) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
                }

                SaleDetails saleDetails = new SaleDetails();
                saleDetails.setSale(savedSale);
                saleDetails.setProduct(product);
                saleDetails.setQuantity(quantity);
                saleDetails.setUnitPrice(product.getPrice());
                saleDetailsRepository.save(saleDetails);

                product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
                productRepository.save(product);
            }

            return ResponseEntity.ok("Venta registrada con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al registrar la venta: " + e.getMessage());
        }
    }
}