package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryCategory;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import co.edu.sena.arkosystem.repository.RepositorySuppliers;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

@Controller
public class ViewInventory {
    @Autowired
    RepositoryInventory inventoryRepository;

    @Autowired
    RepositoryCategory categoryRepository;

    @Autowired
    RepositorySuppliers supplierRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/uploads/";

    private static final String DEFAULT_IMAGE = "/assets/img/uploads/Caja_vacia.jpg";

    @GetMapping("/view/inventory")
    public String list(@RequestParam(required = false) Long categoryId, Model model) {
        model.addAttribute("activePage", "inventory");

        List<Inventory> inventories;
        if (categoryId != null && categoryId > 0) {
            inventories = inventoryRepository.findByCategory_Id(categoryId);
        } else {
            inventories = inventoryRepository.findAll();
        }

        model.addAttribute("inventories", inventories);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);

        return "ViewsInventory/Inventory";
    }

    @GetMapping("viewI/form")
    public String form(Model model) {
        model.addAttribute("activePage", "inventory");
        model.addAttribute("inventory", new Inventory());
        model.addAttribute("category", categoryRepository.findAll());
        model.addAttribute("supplier", supplierRepository.findAll());
        model.addAttribute("inventories", inventoryRepository.findAll());
        return "ViewsInventory/inventory_form";
    }

    @PostMapping("/viewI/save")
    public String save(@ModelAttribute Inventory inventory,
                       @RequestParam(value = "image", required = false) MultipartFile image,
                       RedirectAttributes ra) throws IOException {

        // Guardar imagen si se sube, o asignar la predeterminada
        if (image != null && !image.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, image.getBytes());

            inventory.setImageUrl("/assets/img/uploads/" + fileName);
        } else if (inventory.getImageUrl() == null || inventory.getImageUrl().isEmpty()) {
            inventory.setImageUrl(DEFAULT_IMAGE);
        }

        inventoryRepository.save(inventory);
        ra.addFlashAttribute("success", "Producto guardado correctamente");
        return "redirect:/view/inventory";
    }

    @GetMapping("/viewI/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryRepository.findById(id).orElse(null);
        model.addAttribute("activePage", "inventory");
        model.addAttribute("inventory", inventory);

        model.addAttribute("category", categoryRepository.findAll());
        model.addAttribute("supplier", supplierRepository.findAll());
        return "ViewsInventory/inventory_form";
    }

    @PostMapping("viewI/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        inventoryRepository.deleteById(id);
        ra.addFlashAttribute("success", "Producto eliminado");
        return "redirect:/view/inventory";
    }

    @GetMapping("/viewI/pdf")
    public void exportarPDF(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Inventario.pdf");

        List<Inventory> inventoryList = inventoryRepository.findAll();
        DecimalFormat df = new DecimalFormat("#,###");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Lista de inventario"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Encabezados
        table.addCell("Id");
        table.addCell("Nombre");
        table.addCell("Precio");
        table.addCell("Categoria");
        table.addCell("Stock");
        table.addCell("Proveedor");

        // Filas
        for (Inventory f : inventoryList) {
            table.addCell(f.getId().toString());
            table.addCell(f.getName());
            table.addCell(df.format(f.getPrice()));
            table.addCell(f.getCategory() != null ? f.getCategory().getDescription() : "N/A"); // Protección contra null
            table.addCell(String.valueOf(f.getAvailableQuantity()));
            table.addCell(f.getSupplier() != null ? f.getSupplier().getName() : "N/A"); // Protección contra null
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/viewI/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Inventario.xlsx");

        List<Inventory> inventoryList = inventoryRepository.findAll();
        DecimalFormat df = new DecimalFormat("#,###");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventory");

        // Crear encabezado - Corregido para inventario
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Precio");
        headerRow.createCell(3).setCellValue("Categoria");
        headerRow.createCell(4).setCellValue("Stock");
        headerRow.createCell(5).setCellValue("Proveedor");

        // Agregar datos
        int rowNum = 1;
        for (Inventory inventory : inventoryList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(inventory.getId());
            row.createCell(1).setCellValue(inventory.getName());
            row.createCell(2).setCellValue(df.format(inventory.getPrice()));
            row.createCell(3).setCellValue(inventory.getCategory() != null ? inventory.getCategory().getDescription() : "N/A");
            row.createCell(4).setCellValue(inventory.getAvailableQuantity());
            row.createCell(5).setCellValue(inventory.getSupplier() != null ? inventory.getSupplier().getName() : "N/A");
        }

        // Autoajustar columnas
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
