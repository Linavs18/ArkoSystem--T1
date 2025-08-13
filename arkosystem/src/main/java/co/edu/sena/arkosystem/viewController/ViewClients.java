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

/**
 * Controlador de Spring para la gestión de clientes en el sistema.
 * <p>
 * Esta clase maneja las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) de los clientes,
 * así como la exportación de la lista de clientes a formatos PDF y Excel.
 * Se encarga de inyectar las dependencias del repositorio necesario para interactuar
 * con la base de datos y preparar los datos para las vistas.
 * </p>
 */
@Controller
public class ViewClients
{
    @Autowired
    RepositoryClients clientsRepository;

    /**
     * Muestra la lista de clientes, con la opción de filtrar por nombre.
     * <p>
     * Este método maneja las solicitudes GET a la URL "/view/clients". Si se
     * proporciona un parámetro de búsqueda, se filtran los clientes por nombre;
     * de lo contrario, se muestran todos los clientes.
     * </p>
     *
     * @param search Un término de búsqueda para filtrar clientes por nombre (opcional).
     * @param model El objeto {@link Model} para agregar la lista de clientes a la vista.
     * @return El nombre de la plantilla de la vista para la lista de clientes.
     */
    @GetMapping("/view/clients")
    public String list(@org.springframework.web.bind.annotation.RequestParam(name = "search", required = false) String search, Model model)
    {
        model.addAttribute("activePage", "clients");
        if (search != null && !search.isEmpty()) {
            model.addAttribute("clients", clientsRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("clients", clientsRepository.findAll());
        }
        model.addAttribute("search", search);
        return "ViewsClients/Clients";
    }

    /**
     * Muestra el formulario para crear un nuevo cliente.
     * <p>
     * Este método prepara un objeto {@link Clients} vacío para que el formulario
     * pueda ser completado con los datos de un nuevo cliente.
     * </p>
     *
     * @param model El objeto {@link Model} para agregar el objeto cliente a la vista.
     * @return El nombre de la plantilla de la vista para el formulario de clientes.
     */
    @GetMapping("viewC/form")
    public String form(Model model)
    {
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", new Clients());
        return "ViewsClients/clients_form";
    }

    /**
     * Guarda un nuevo cliente o actualiza uno existente.
     * <p>
     * Este método recibe un objeto {@link Clients} del formulario y lo guarda
     * en la base de datos a través del repositorio.
     * </p>
     *
     * @param clients El objeto {@link Clients} a guardar.
     * @param ra El objeto {@link RedirectAttributes} para pasar mensajes de éxito en la redirección.
     * @return Una redirección a la lista de clientes.
     */
    @PostMapping("/viewC/save")
    public String save(@ModelAttribute Clients clients, RedirectAttributes ra)
    {
        clientsRepository.save(clients);
        ra.addFlashAttribute("success","Cliente Guardado");
        return "redirect:/view/clients";
    }

    /**
     * Muestra el formulario para editar un cliente específico.
     * <p>
     * Este método recupera un cliente por su ID y lo añade al modelo para que el
     * formulario pueda ser precargado con sus datos.
     * </p>
     *
     * @param id El ID del cliente a editar.
     * @param model El objeto {@link Model} para agregar el cliente a la vista.
     * @return El nombre de la plantilla de la vista para el formulario de clientes.
     */
    @GetMapping("/viewC/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Clients clients = clientsRepository.findById(id).orElse(null);
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", clients);
        return "ViewsClients/clients_form";
    }

    /**
     * Elimina un cliente de la base de datos por su ID.
     * <p>
     * Este método maneja las solicitudes POST para eliminar un cliente y luego
     * redirige a la lista de clientes con un mensaje de éxito.
     * </p>
     *
     * @param id El ID del cliente a eliminar.
     * @param ra El objeto {@link RedirectAttributes} para pasar mensajes de éxito en la redirección.
     * @return Una redirección a la lista de clientes.
     */
    @PostMapping("viewC/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        clientsRepository.deleteById(id);
        ra.addFlashAttribute("success", "Cliente eliminado");
        return "redirect:/view/clients";
    }

    /**
     * Exporta la lista de clientes completa a un archivo PDF.
     *
     * @param response El objeto {@link HttpServletResponse} para escribir el archivo PDF.
     * @throws Exception Si ocurre un error durante la generación del PDF.
     */
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

    /**
     * Exporta la lista de clientes completa a un archivo Excel.
     *
     * @param response El objeto {@link HttpServletResponse} para escribir el archivo Excel.
     * @throws Exception Si ocurre un error durante la generación del archivo Excel.
     */
    @GetMapping("/viewC/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Clientes.xlsx");

        List<Clients> clientsList = clientsRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes");

        // Crear encabezado
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