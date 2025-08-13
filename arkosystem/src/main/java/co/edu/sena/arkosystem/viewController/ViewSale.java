package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view/sales")
public class ViewSale {

    @Autowired private RepositorySale saleRepository;
    @Autowired private RepositorySaleDetails saleDetailsRepository;
    @Autowired private RepositoryInventory productRepository;
    @Autowired private RepositoryClients clientsRepository;
    @Autowired private RepositoryUser userRepository;
    @Autowired private RepositoryEmployee employeeRepository;

    @GetMapping
    public String listSales(Model model, HttpSession session) {
        model.addAttribute("activePage", "sales");
        model.addAttribute("clients", clientsRepository.findAll());
        model.addAttribute("products", productRepository.findAll()
                .stream()
                .filter(p -> p.getAvailableQuantity() > 0)
                .collect(Collectors.toList()));
        model.addAttribute("employees", employeeRepository.findAll());

        List<Map<String, Object>> cart = getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("total", calculateTotal(cart));

        model.addAttribute("selectedClient", session.getAttribute("selectedClient"));
        model.addAttribute("selectedEmployee", session.getAttribute("selectedEmployee"));
        model.addAttribute("selectedPayment", session.getAttribute("selectedPayment"));

        model.addAttribute("recentSales", saleRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getSaleDate().compareTo(a.getSaleDate()))
                .limit(10)
                .collect(Collectors.toList()));

        return "ViewSale/Sales";
    }

    @PostMapping
    public String handleAction(
            @RequestParam String action,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Integer quantity,
            HttpSession session,
            RedirectAttributes ra,
            HttpServletRequest request) {
        try {
            if (clientId != null) session.setAttribute("selectedClient", clientId);
            if (employeeId != null) session.setAttribute("selectedEmployee", employeeId);
            if (paymentMethod != null && !paymentMethod.isEmpty()) session.setAttribute("selectedPayment", paymentMethod);

            switch (action) {
                case "add":
                    return addToCart(productId, quantity, session, ra);
                case "remove":
                    return removeFromCart(productId, session, ra);
                case "clear":
                    return clearCart(session, ra);
                case "register":
                    return registerSale(session, ra, request);
                default:
                    ra.addFlashAttribute("error", "Acción no válida");
                    return "redirect:/view/sales";
            }
        } catch (Exception e) {
            e.printStackTrace(); // para depuración en consola
            ra.addFlashAttribute("error", "Ocurrió un error: " + e.getMessage());
            return "redirect:/view/sales";
        }
    }

    private String addToCart(Long productId, Integer quantity, HttpSession session, RedirectAttributes ra) {
        if (productId == null || quantity == null || quantity <= 0) {
            ra.addFlashAttribute("error", "Datos inválidos para añadir producto");
            return "redirect:/view/sales";
        }

        Inventory product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getAvailableQuantity() < quantity) {
            ra.addFlashAttribute("error", "Stock insuficiente. Disponible: " + product.getAvailableQuantity());
            return "redirect:/view/sales";
        }

        List<Map<String, Object>> cart = getCart(session);
        Optional<Map<String, Object>> existing = cart.stream()
                .filter(i -> i.get("id").equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            int newQty = (Integer) existing.get().get("quantity") + quantity;
            if (newQty > product.getAvailableQuantity()) {
                ra.addFlashAttribute("error", "No hay suficiente stock");
                return "redirect:/view/sales";
            }
            existing.get().put("quantity", newQty);
        } else {
            Map<String, Object> item = new HashMap<>();
            item.put("id", product.getId());
            item.put("name", product.getName());
            item.put("price", product.getPrice());
            item.put("quantity", quantity);
            cart.add(item);
        }

        session.setAttribute("cart", cart);
        ra.addFlashAttribute("success", "Producto agregado");
        return "redirect:/view/sales";
    }

    private String removeFromCart(Long productId, HttpSession session, RedirectAttributes ra) {
        List<Map<String, Object>> cart = getCart(session);
        if (cart.removeIf(i -> i.get("id").equals(productId))) {
            session.setAttribute("cart", cart);
            ra.addFlashAttribute("success", "Producto eliminado");
        } else {
            ra.addFlashAttribute("error", "Producto no encontrado en el carrito");
        }
        return "redirect:/view/sales";
    }

    private String clearCart(HttpSession session, RedirectAttributes ra) {
        session.setAttribute("cart", new ArrayList<>());
        ra.addFlashAttribute("success", "Carrito vacío");
        return "redirect:/view/sales";
    }

    @Transactional
    private String registerSale(HttpSession session, RedirectAttributes ra, HttpServletRequest request) {

        try {
            List<Map<String, Object>> cart = getCart(session);
            if (cart.isEmpty()) {
                ra.addFlashAttribute("error", "El carrito está vacío");
                return "redirect:/view/sales";
            }



            Long clientId = (Long) session.getAttribute("selectedClient");
            String paymentMethod = (String) session.getAttribute("selectedPayment");

            if (clientId == null) {
                ra.addFlashAttribute("error", "Debe seleccionar un cliente");
                return "redirect:/view/sales";
            }
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                ra.addFlashAttribute("error", "Debe seleccionar un método de pago");
                return "redirect:/view/sales";
            }

            String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepository.findByEmail(emailUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Employee employee = employeeRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

            Clients client = clientsRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            BigDecimal total = calculateTotal(cart);




            Sale sale = new Sale();
            sale.setClient(client);
            sale.setEmployee(employee);
            sale.setSaleDate(LocalDateTime.now());
            sale.setPaymentMethod(paymentMethod);
            sale.setStatus("COMPLETED");
            sale.setTotal(total);

            saleRepository.save(sale);

            for (Map<String, Object> item : cart) {
                Inventory product = productRepository.findById((Long) item.get("id"))
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                if (product.getAvailableQuantity() < (Integer) item.get("quantity")) {
                    throw new RuntimeException("Stock insuficiente para " + product.getName());
                }

                SaleDetails detail = new SaleDetails();
                detail.setSale(sale);
                detail.setProduct(product);
                detail.setQuantity((Integer) item.get("quantity"));
                detail.setUnitPrice((BigDecimal) item.get("price"));

                saleDetailsRepository.save(detail);

                product.setAvailableQuantity(product.getAvailableQuantity() - (Integer) item.get("quantity"));
                productRepository.save(product);


            }

            session.removeAttribute("cart");
            session.removeAttribute("selectedClient");
            session.removeAttribute("selectedEmployee");
            session.removeAttribute("selectedPayment");



            ra.addFlashAttribute("success", "Venta registrada con éxito. ID: " + sale.getId());
            return "redirect:/view/sales";
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al registrar la venta: " + e.getMessage());
            return "redirect:/view/sales";
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getCart(HttpSession session) {
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private BigDecimal calculateTotal(List<Map<String, Object>> cart) {
        return cart.stream()
                .map(i -> ((BigDecimal) i.get("price")).multiply(BigDecimal.valueOf((Integer) i.get("quantity"))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
