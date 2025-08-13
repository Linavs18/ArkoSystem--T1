package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/pdf")
    public void exportarPDF(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Ventas.pdf");

        List<Sale> saleList = saleRepository.findAll();
        DecimalFormat df = new DecimalFormat("#,###");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Lista de Ventas"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Encabezados
        table.addCell("Id");
        table.addCell("Fecha");
        table.addCell("Cliente");
        table.addCell("Empleado");
        table.addCell("Productos");
        table.addCell("Total");
        table.addCell("Estadoo");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Filas
        for (Sale f : saleList) {
            table.addCell(f.getId().toString());
            table.addCell(f.getSaleDate().format(formatter));
            table.addCell(f.getClient().getName());
            table.addCell(f.getEmployee().getName());
            table.addCell(String.valueOf(f.getSaleDetails().size()));
            table.addCell(df.format(f.getTotal()));
            table.addCell(f.getStatus());

        }

        document.add(table);
        document.close();
    }

    @GetMapping("/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Ventas.xlsx");

        List<Sale> saleList = saleRepository.findAll();
        DecimalFormat df = new DecimalFormat("#,###");

        List<Sale> salesList = saleRepository.findAll();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventory");

        // Crear encabezado - Corregido para inventario
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Fecha");
        headerRow.createCell(2).setCellValue("Cliente");
        headerRow.createCell(3).setCellValue("Empleado");
        headerRow.createCell(4).setCellValue("Productos");
        headerRow.createCell(5).setCellValue("Total");
        headerRow.createCell(6).setCellValue("Estado");


        // Agregar datos
        int rowNum = 1;
        for (Sale sale : salesList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sale.getId());
            row.createCell(1).setCellValue(sale.getSaleDate().format(dateFormatter));
            row.createCell(2).setCellValue(sale.getClient() != null ? sale.getClient().getName() : "N/A");
            row.createCell(3).setCellValue(sale.getEmployee() != null ? sale.getEmployee().getName() : "N/A");
            row.createCell(4).setCellValue(sale.getSaleDetails() != null ? sale.getSaleDetails().size() : 0);
            row.createCell(5).setCellValue(sale.getTotal() != null ? sale.getTotal().doubleValue() : 0.0);
            row.createCell(6).setCellValue(sale.getStatus());
        }

        // Autoajustar columnas
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
