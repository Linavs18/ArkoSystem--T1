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

/**
 * Controlador REST para la generación de reportes.
 * <p>
 * Esta clase proporciona endpoints para generar y exportar diferentes tipos de reportes,
 * como la lista de productos con bajo stock, en formatos PDF y Excel.
 * </p>
 */
@RestController
@RequestMapping("/api/reports")
public class ControllerReports {

    @Autowired
    private RepositoryInventory inventoryRepository;

    /**
     * Exporta la lista de productos con bajo stock en formato PDF.
     * <p>
     * Responde a las peticiones GET a "/api/reports/low-stock/pdf".
     * Genera un documento PDF que contiene una tabla con la información de los productos
     * cuyo stock disponible es menor o igual a su stock mínimo.
     * </p>
     *
     * @param response El objeto {@link HttpServletResponse} para escribir el contenido del archivo.
     * @throws Exception Si ocurre un error durante la generación del PDF.
     */
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

    /**
     * Exporta la lista de productos con bajo stock en formato Excel.
     * <p>
     * Responde a las peticiones GET a "/api/reports/low-stock/excel".
     * Genera un libro de trabajo de Excel (XLSX) con una hoja de cálculo que contiene
     * los productos cuyo stock disponible es menor o igual a su stock mínimo.
     * </p>
     *
     * @param response El objeto {@link HttpServletResponse} para escribir el contenido del archivo.
     * @throws IOException Si ocurre un error durante la generación del archivo Excel.
     */
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
