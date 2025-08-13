package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import co.edu.sena.arkosystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public String listSales(Model model, HttpSession session, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("activePage", "sales");

        if (userDetails != null) {
            model.addAttribute("currentUsername", userDetails.getUsername());
        }

        List<Clients> allClients = clientsRepository.findAll();
        List<Inventory> availableProducts = productRepository.findAll()
                .stream()
                .filter(p -> p.getAvailableQuantity() > 0)
                .collect(Collectors.toList());

        model.addAttribute("clientNames", allClients.stream().map(Clients::getName).collect(Collectors.toList()));
        model.addAttribute("products", availableProducts);

        // Carrito
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);

        // Cliente y pago seleccionados
        model.addAttribute("selectedClient", session.getAttribute("selectedClient"));
        model.addAttribute("selectedPayment", session.getAttribute("selectedPayment"));

        double total = cart.stream().mapToDouble(item ->
                (double) item.get("price") * (int) item.get("quantity")
        ).sum();
        model.addAttribute("total", total);

        return "ViewSale/Sales";
    }

    @PostMapping("/sales")
    public String handleSale(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Integer quantity,
            HttpSession session,
            @AuthenticationPrincipal  UserDetailsImpl userDetails,
            RedirectAttributes redirectAttrs
    ) {
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        try {
            // Guardar cliente y pago
            if (client != null && !client.isEmpty()) session.setAttribute("selectedClient", client);
            if (paymentMethod != null && !paymentMethod.isEmpty()) session.setAttribute("selectedPayment", paymentMethod);
            if ("add".equals(action) && productId != null && quantity != null) {
                Inventory product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                if (product.getAvailableQuantity() < quantity) {
                    redirectAttrs.addFlashAttribute("error",
                            "Stock insuficiente para el producto: " + product.getName() +
                                    ". Disponible: " + product.getAvailableQuantity());
                    return "redirect:/view/sales";
                }

                Map<String, Object> item = new HashMap<>();
                item.put("id", product.getId());
                item.put("name", product.getName());
                item.put("price", product.getPrice());
                item.put("quantity", quantity);
                cart.add(item);
            }
            if ("add".equals(action) && productId != null && quantity != null) {
                Inventory product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                if (product.getAvailableQuantity() < quantity) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
                }
                Map<String, Object> item = new HashMap<>();
                item.put("id", product.getId());
                item.put("name", product.getName());
                item.put("price", product.getPrice());
                item.put("quantity", quantity);
                cart.add(item);

            } else if ("remove".equals(action) && productId != null) {
                cart.removeIf(p -> p.get("id").equals(productId));

            } else if ("clear".equals(action)) {
                cart.clear();

            } else if ("register".equals(action)) {
                if (cart.isEmpty()) throw new RuntimeException("No hay productos en el carrito.");
                if (client == null || client.isEmpty()) throw new RuntimeException("Debe seleccionar un cliente.");
                if (paymentMethod == null || paymentMethod.isEmpty()) throw new RuntimeException("Debe seleccionar un método de pago.");

                Clients selectedClient = clientsRepository.findByName(client)
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + client));

                Users user = userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userDetails.getUsername()));

                Employee employee = employeeRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new RuntimeException("Empleado no encontrado para el usuario: " + user.getId()));

                BigDecimal total = cart.stream()
                        .map(itemCart -> {
                            BigDecimal price = new BigDecimal(itemCart.get("price").toString());
                            Integer quantityInteger = (Integer) itemCart.get("quantity");
                            return price.multiply(BigDecimal.valueOf(quantityInteger));
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Sale newSale = new Sale();
                newSale.setClient(selectedClient);
                newSale.setSaleDate(LocalDateTime.now());
                newSale.setTotal(total);
                newSale.setPaymentMethod(paymentMethod);
                newSale.setEmployee(employee);
                newSale.setStatus("COMPLETED");

                Sale savedSale = saleRepository.save(newSale);

                for (Map<String, Object> item : cart) {
                    Inventory product = productRepository.findById((Long) item.get("id"))
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    SaleDetails saleDetails = new SaleDetails();
                    saleDetails.setSale(savedSale);
                    saleDetails.setProduct(product);
                    saleDetails.setQuantity((int) item.get("quantity"));
                    saleDetails.setUnitPrice((BigDecimal) item.get("price"));
                    saleDetailsRepository.save(saleDetails);

                    product.setAvailableQuantity(product.getAvailableQuantity() - (int) item.get("quantity"));
                    productRepository.save(product);
                }

                cart.clear();
                session.removeAttribute("selectedClient");
                session.removeAttribute("selectedPayment");
                redirectAttrs.addFlashAttribute("success", "Venta registrada correctamente.");
            }
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }

        session.setAttribute("cart", cart);
        return "redirect:/view/sales";
    }

}
