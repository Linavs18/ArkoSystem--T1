package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
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
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ControllerReports {

    @Autowired
    private RepositoryInventory inventoryRepository;

    //Reporte de bajo stock
    @GetMapping("/low-stock")
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }

    //Reporte de bajo stock en Excel
    @GetMapping("/low-stock/excel")
    public void exportLowStockToExcel(HttpServletResponse response) throws IOException {
        List<Inventory> lowStockItems = inventoryRepository.findLowStockItems();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bajo Stock");

        String[] headers = {"ID", "Nombre", "Cantidad Disponible", "Stock Mínimo", "Precio"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Inventory item : lowStockItems) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getName());
            row.createCell(2).setCellValue(item.getAvailableQuantity());
            row.createCell(3).setCellValue(item.getMinStock());
            row.createCell(4).setCellValue(item.getPrice());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_bajo_stock.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
