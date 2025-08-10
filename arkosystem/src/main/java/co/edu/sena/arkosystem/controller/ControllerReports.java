package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ControllerReports {

    @Autowired
    private RepositoryInventory inventoryRepository;

    @GetMapping("/low-stock/pdf")
    public void exportLowStockPdf(HttpServletResponse response) throws Exception {
        List<Inventory> lowStockList = inventoryRepository.findLowStockItems();
        DecimalFormat df = new DecimalFormat("#,###");

        // Configuración de respuesta
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=low_stock.pdf");

        // Crear documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Reporte - Productos en Bajo Stock"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Encabezados
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Precio");
        table.addCell("Stock Disponible");
        table.addCell("Stock Mínimo");

        // Filas
        for (Inventory item : lowStockList) {
            table.addCell(String.valueOf(item.getId()));
            table.addCell(item.getName());
            table.addCell(df.format(item.getPrice()));
            table.addCell(String.valueOf(item.getAvailableQuantity()));
            table.addCell(String.valueOf(item.getMinStock()));
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/low-stock/excel")
    public void exportLowStockExcel(HttpServletResponse response) throws IOException {
        List<Inventory> lowStockList = inventoryRepository.findLowStockItems();
        DecimalFormat df = new DecimalFormat("#,###");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bajo Stock");

        String[] headers = {"ID", "Nombre", "Precio", "Stock Disponible", "Stock Mínimo"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Inventory item : lowStockList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getName());
            row.createCell(2).setCellValue(df.format(item.getPrice()));
            row.createCell(3).setCellValue(item.getAvailableQuantity());
            row.createCell(4).setCellValue(item.getMinStock());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=low_stock.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
