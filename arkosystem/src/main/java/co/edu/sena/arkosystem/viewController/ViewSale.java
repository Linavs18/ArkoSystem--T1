package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import co.edu.sena.arkosystem.security.UserDetailsImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        try {
            List<Clients> allClients = clientsRepository.findAll();

            List<Inventory> availableProducts = productRepository.findAll()
                    .stream()
                    .filter(p -> p.getAvailableQuantity() > 0)
                    .collect(Collectors.toList());

            List<Employee> employees = employeeRepository.findAll();

            model.addAttribute("clients", allClients);
            model.addAttribute("products", availableProducts);
            model.addAttribute("employees", employees);

            List<Map<String, Object>> cart = getCartFromSession(session);
            model.addAttribute("cart", cart);

            model.addAttribute("selectedClient", session.getAttribute("selectedClient"));
            model.addAttribute("selectedEmployee", session.getAttribute("selectedEmployee"));
            model.addAttribute("selectedPayment", session.getAttribute("selectedPayment"));

            BigDecimal total = calculateCartTotal(cart);
            model.addAttribute("total", total);

            List<Sale> recentSales = saleRepository.findAll()
                    .stream()
                    .sorted((s1, s2) -> s2.getSaleDate().compareTo(s1.getSaleDate()))
                    .limit(10)
                    .collect(Collectors.toList());
            model.addAttribute("recentSales", recentSales);

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los datos: " + e.getMessage());
        }

        return "ViewSale/Sales";
    }

    @PostMapping("/sales")
    @Transactional
    public String handleSale(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Integer quantity,
            HttpSession session,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            RedirectAttributes redirectAttrs
    ) {
        List<Map<String, Object>> cart = getCartFromSession(session);

        try {
            if (clientId != null) session.setAttribute("selectedClient", clientId);
            if (employeeId != null) session.setAttribute("selectedEmployee", employeeId);
            if (paymentMethod != null && !paymentMethod.isEmpty()) session.setAttribute("selectedPayment", paymentMethod);

            switch (action != null ? action : "") {
                case "add": return handleAddToCart(productId, quantity, cart, session, redirectAttrs);
                case "remove": return handleRemoveFromCart(productId, cart, session, redirectAttrs);
                case "clear": return handleClearCart(session, redirectAttrs);
                case "register": return handleRegisterSale(cart, clientId, employeeId, paymentMethod, session, userDetails, redirectAttrs);
                default: redirectAttrs.addFlashAttribute("error", "Acción no válida");
            }

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/view/sales";
    }

    private String handleAddToCart(Long productId, Integer quantity, List<Map<String, Object>> cart,
                                 HttpSession session, RedirectAttributes redirectAttrs) {
        if (productId == null || quantity == null || quantity <= 0) {
            redirectAttrs.addFlashAttribute("error", "Datos de producto inválidos");
            return "redirect:/view/sales";
        }

        Optional<Inventory> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            redirectAttrs.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/view/sales";
        }

        Inventory product = productOpt.get();

        if (product.getAvailableQuantity() < quantity) {
            redirectAttrs.addFlashAttribute("error",
                    "Stock insuficiente para " + product.getName() +
                            ". Disponible: " + product.getAvailableQuantity());
            return "redirect:/view/sales";
        }

        Optional<Map<String, Object>> existingItem = cart.stream()
                .filter(item -> productId.equals(item.get("id")))
                .findFirst();

        if (existingItem.isPresent()) {
            Map<String, Object> item = existingItem.get();
            int newQuantity = (Integer) item.get("quantity") + quantity;

            if (newQuantity > product.getAvailableQuantity()) {
                redirectAttrs.addFlashAttribute("error",
                        "No se puede agregar más cantidad. Stock disponible: " + product.getAvailableQuantity());
                return "redirect:/view/sales";
            }

            item.put("quantity", newQuantity);
        } else {
            Map<String, Object> item = new HashMap<>();
            item.put("id", product.getId());
            item.put("name", product.getName());
            item.put("price", product.getPrice());
            item.put("quantity", quantity);
            cart.add(item);
        }

        session.setAttribute("cart", cart);
        redirectAttrs.addFlashAttribute("success", "Producto agregado al carrito");
        return "redirect:/view/sales";
    }

    private String handleRemoveFromCart(Long productId, List<Map<String, Object>> cart,
                                      HttpSession session, RedirectAttributes redirectAttrs) {
        if (productId == null) {
            redirectAttrs.addFlashAttribute("error", "ID de producto inválido");
            return "redirect:/view/sales";
        }

        boolean removed = cart.removeIf(item -> productId.equals(item.get("id")));

        if (removed) {
            session.setAttribute("cart", cart);
            redirectAttrs.addFlashAttribute("success", "Producto eliminado del carrito");
        } else {
            redirectAttrs.addFlashAttribute("error", "Producto no encontrado en el carrito");
        }

        return "redirect:/view/sales";
    }

    private String handleClearCart(HttpSession session, RedirectAttributes redirectAttrs) {
        session.setAttribute("cart", new ArrayList<>());
        redirectAttrs.addFlashAttribute("success", "Carrito vaciado");
        return "redirect:/view/sales";
    }

    @Transactional
    private String handleRegisterSale(List<Map<String, Object>> cart, Long clientId, Long employeeId,
                                    String paymentMethod, HttpSession session, UserDetailsImpl userDetails,
                                    RedirectAttributes redirectAttrs) {

        if (cart.isEmpty()) throw new RuntimeException("No hay productos en el carrito");
        if (clientId == null) throw new RuntimeException("Debe seleccionar un cliente");
        if (paymentMethod == null || paymentMethod.isEmpty()) throw new RuntimeException("Debe seleccionar un método de pago");

        Optional<Clients> clientOpt = clientsRepository.findById(clientId);
        if (!clientOpt.isPresent()) throw new RuntimeException("Cliente no encontrado");

        Employee employee = null;
        if (employeeId != null) {
            employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        } else {
            Optional<Users> userOpt = userRepository.findByEmail(userDetails.getUsername());
            if (userOpt.isPresent()) {
                employee = employeeRepository.findByUserId(userOpt.get().getId()).orElse(null);
            }
            if (employee == null) throw new RuntimeException("No se pudo determinar el empleado");
        }

        BigDecimal total = calculateCartTotal(cart);

        Sale newSale = new Sale();
        newSale.setClient(clientOpt.get());
        newSale.setEmployee(employee);
        newSale.setSaleDate(LocalDateTime.now());
        newSale.setTotal(total);
        newSale.setPaymentMethod(paymentMethod);
        newSale.setStatus("COMPLETED");

        Sale savedSale = saleRepository.save(newSale);

        for (Map<String, Object> item : cart) {
            Long productId = (Long) item.get("id");
            Integer itemQuantity = (Integer) item.get("quantity");
            BigDecimal itemPrice = (BigDecimal) item.get("price");

            Inventory product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (product.getAvailableQuantity() < itemQuantity) {
                throw new RuntimeException("Stock insuficiente para " + product.getName());
            }

            SaleDetails saleDetail = new SaleDetails();
            saleDetail.setSale(savedSale);
            saleDetail.setProduct(product);
            saleDetail.setQuantity(itemQuantity);
            saleDetail.setUnitPrice(itemPrice);
            saleDetailsRepository.save(saleDetail);

            product.setAvailableQuantity(product.getAvailableQuantity() - itemQuantity);
            productRepository.save(product);
        }

        session.setAttribute("cart", new ArrayList<>());
        session.removeAttribute("selectedClient");
        session.removeAttribute("selectedEmployee");
        session.removeAttribute("selectedPayment");

        redirectAttrs.addFlashAttribute("success",
                "Venta registrada correctamente. ID: " + savedSale.getId() + " - Total: $" + total);

        return "redirect:/view/sales";
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getCartFromSession(HttpSession session) {
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private BigDecimal calculateCartTotal(List<Map<String, Object>> cart) {
        return cart.stream()
                .map(item -> ((BigDecimal) item.get("price")).multiply(BigDecimal.valueOf((Integer) item.get("quantity"))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}