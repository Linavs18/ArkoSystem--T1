package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.Employee;
import co.edu.sena.arkosystem.repository.RepositoryEmployee;
import co.edu.sena.arkosystem.repository.RepositoryRole;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de Spring para la gestión de empleados en el sistema.
 * <p>
 * Esta clase maneja las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * de los empleados, permitiendo su visualización, creación, edición y eliminación.
 * Se encarga de inyectar los repositorios necesarios para interactuar con la base de datos
 * y preparar los datos para las vistas.
 * </p>
 */
@Controller
public class ViewEmployee {
    @Autowired
    RepositoryEmployee employeeRepository;

    @Autowired
    RepositoryRole roleRepository;

    @Autowired
    RepositoryUser userRepository;

    /**
     * Muestra la lista de empleados, con la opción de filtrar por nombre.
     * <p>
     * Este método maneja las solicitudes GET a la URL "/view/employee". Si se
     * proporciona un parámetro de búsqueda, se filtran los empleados por nombre;
     * de lo contrario, se muestran todos los empleados.
     * </p>
     *
     * @param search Un término de búsqueda para filtrar empleados por nombre (opcional).
     * @param model El objeto {@link Model} para agregar la lista de empleados a la vista.
     * @return El nombre de la plantilla de la vista para la lista de empleados.
     */
    @GetMapping("/view/employee")
    public String list(@org.springframework.web.bind.annotation.RequestParam(name = "search", required = false) String search, Model model) {
        model.addAttribute("activePage", "employee");
        if (search != null && !search.isEmpty()) {
            model.addAttribute("employees", employeeRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("employees", employeeRepository.findAll());
        }
        model.addAttribute("search", search);
        return "ViewsEmployees/Employee";
    }

    /**
     * Muestra el formulario para crear un nuevo empleado.
     * <p>
     * Este método prepara un objeto {@link Employee} vacío y las listas de roles
     * y usuarios disponibles para que el formulario pueda ser completado.
     * </p>
     *
     * @param model El objeto {@link Model} para agregar los datos del formulario a la vista.
     * @return El nombre de la plantilla de la vista para el formulario de empleados.
     */
    @GetMapping("viewE/form")
    public String form(Model model) {
        model.addAttribute("activePage", "employee");
        model.addAttribute("employee", new Employee());
        model.addAttribute("role", roleRepository.findAll());
        model.addAttribute("user", userRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        return "ViewsEmployees/employee_form";
    }

    /**
     * Guarda un nuevo empleado o actualiza uno existente.
     * <p>
     * Este método recibe un objeto {@link Employee} del formulario y lo guarda
     * en la base de datos a través del repositorio.
     * </p>
     *
     * @param employee El objeto {@link Employee} a guardar.
     * @param ra El objeto {@link RedirectAttributes} para pasar mensajes de éxito en la redirección.
     * @return Una redirección a la lista de empleados.
     */
    @PostMapping("/viewE/save")
    public String save(@ModelAttribute Employee employee, RedirectAttributes ra) {
        employeeRepository.save(employee);
        ra.addFlashAttribute("success", "Empleado guardado correctamente");
        return "redirect:/view/employee";
    }

    /**
     * Muestra el formulario para editar un empleado específico.
     * <p>
     * Este método recupera un empleado por su ID y lo añade al modelo, junto con
     * las listas de roles y usuarios, para que el formulario pueda ser precargado.
     * </p>
     *
     * @param id El ID del empleado a editar.
     * @param model El objeto {@link Model} para agregar el empleado y los datos de apoyo a la vista.
     * @return El nombre de la plantilla de la vista para el formulario de empleados.
     */
    @GetMapping("/viewE/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("activePage", "employee");
        model.addAttribute("employee", employee);
        model.addAttribute("role", roleRepository.findAll());
        model.addAttribute("user", userRepository.findAll());
        return "ViewsEmployees/employee_form";
    }

    /**
     * Elimina un empleado de la base de datos por su ID.
     * <p>
     * Este método maneja las solicitudes POST para eliminar un empleado y luego
     * redirige a la lista de empleados con un mensaje de éxito.
     * </p>
     *
     * @param id El ID del empleado a eliminar.
     * @param ra El objeto {@link RedirectAttributes} para pasar mensajes de éxito en la redirección.
     * @return Una redirección a la lista de empleados.
     */
    @PostMapping("viewE/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        employeeRepository.deleteById(id);
        ra.addFlashAttribute("success", "Empleado eliminado");
        return "redirect:/view/employee";
    }
}