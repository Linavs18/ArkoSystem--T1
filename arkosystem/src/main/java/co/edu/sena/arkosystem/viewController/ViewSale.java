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
    public String listSales(Model model, HttpSession session,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // Verificación obligatoria de autenticación
        if (userDetails == null) {
            return "redirect:/login";
        }

        model.addAttribute("activePage", "sales");
        model.addAttribute("currentUsername", userDetails.getUsername());

        // Verificar relación usuario-empleado
        Users user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        boolean hasEmployee = user != null && employeeRepository.existsByUserId(user.getId());
        model.addAttribute("hasEmployee", hasEmployee);

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

        // Total
        BigDecimal total = cart.stream()
                .map(item -> ((BigDecimal) item.get("price"))
                        .multiply(BigDecimal.valueOf((int) item.get("quantity"))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            RedirectAttributes redirectAttrs) {

        // Verificación de autenticación
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        try {
            // Resto del código de manejo de ventas...
            // (Mantener igual que en tu versión original)

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }

        session.setAttribute("cart", cart);
        return "redirect:/view/sales";
    }
}