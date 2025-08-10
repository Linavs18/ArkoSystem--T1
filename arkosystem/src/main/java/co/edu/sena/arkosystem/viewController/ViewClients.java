package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.Clients;
import co.edu.sena.arkosystem.repository.RepositoryClients;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.List;

@Controller
public class ViewClients
{
    @Autowired
    RepositoryClients clientsRepository;

    @GetMapping("/view/clients")
    public String list(Model model)
    {
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", clientsRepository.findAll());
        return "ViewsClients/Clients";
    }

    @GetMapping("viewC/form")
    public String form(Model model)
    {
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", new Clients());
        return "ViewsClients/clients_form";
    }

    @PostMapping("/viewC/save")
    public String save(@ModelAttribute Clients clients, RedirectAttributes ra)
    {
        clientsRepository.save(clients);
        ra.addFlashAttribute("success","Cliente Guardado");
        return "redirect:/view/clients";
    }

    @GetMapping("/viewC/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Clients clients = clientsRepository.findById(id).orElse(null);
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", clients);
        return "ViewsClients/clients_form";
    }

    @PostMapping("viewC/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        clientsRepository.deleteById(id);
        ra.addFlashAttribute("success", "Cliente eliminado");
        return "redirect:/view/clients";
    }

    @GetMapping("/viewC/pdf")
    public void exportarPDF(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Clientes.pdf");

        List<Clients> clientsList = clientsRepository.findAll();

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Lista de clientes"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Encabezados
        table.addCell("Id");
        table.addCell("Nombre");
        table.addCell("Teléfono");
        table.addCell("Dirección");
        table.addCell("Correo");

        // Filas
        for (Clients f : clientsList) {
            table.addCell(f.getId().toString());
            table.addCell(f.getName());
            table.addCell(f.getPhone());
            table.addCell(f.getAddress());
            table.addCell(f.getEmail());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/viewC/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Clientes.xlsx");

        List<Clients> clientsList = clientsRepository.findAll();;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes");

        // Crear encabezado - Corregido para inventario
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Id");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Teléfono");
        headerRow.createCell(3).setCellValue("Dirección");
        headerRow.createCell(4).setCellValue("Correo");

        // Agregar datos
        int rowNum = 1;
        for (Clients clients: clientsList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(clients.getId());
            row.createCell(1).setCellValue(clients.getName());
            row.createCell(2).setCellValue(clients.getPhone());
            row.createCell(3).setCellValue(clients.getAddress());
            row.createCell(4).setCellValue(clients.getEmail());
        }

        // Autoajustar columnas
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
