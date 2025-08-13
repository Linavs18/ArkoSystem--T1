package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.*;
import co.edu.sena.arkosystem.repository.*;
import co.edu.sena.arkosystem.security.UserDetailsImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
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

        // Total como BigDecimal
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
                item.put("price", product.getPrice()); // BigDecimal
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

                // Calcular total con BigDecimal
                BigDecimal total = cart.stream()
                        .map(item -> ((BigDecimal) item.get("price"))
                                .multiply(BigDecimal.valueOf((int) item.get("quantity"))))
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

    @GetMapping("/sales/pdf")
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

    @GetMapping("/sales/excel")
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
