package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.Suppliers;
import co.edu.sena.arkosystem.repository.RepositorySuppliers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de Spring para la gestión de proveedores en la aplicación.
 * <p>
 * Esta clase maneja las solicitudes HTTP relacionadas con la creación, lectura,
 * actualización y eliminación (CRUD) de proveedores. Utiliza un repositorio
 * para interactuar con la base de datos y un modelo para pasar datos a las vistas.
 * </p>
 */
@Controller
@RequestMapping("/view/suppliers")
public class ViewSuppliers {

    @Autowired
    private RepositorySuppliers supplierRepository;

    /**
     * Muestra una lista de todos los proveedores o una lista filtrada por nombre.
     * <p>
     * Este método maneja las solicitudes GET a la URL "/view/suppliers".
     * Si se proporciona un parámetro de búsqueda, se filtran los proveedores.
     * De lo contrario, se muestran todos los proveedores.
     * </p>
     *
     * @param search Un término de búsqueda para filtrar proveedores por nombre (opcional).
     * @param model El objeto {@link Model} para agregar la lista de proveedores a la vista.
     * @return El nombre de la plantilla de vista para la lista de proveedores.
     */
    @GetMapping
    public String list(@org.springframework.web.bind.annotation.RequestParam(name = "search", required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("suppliers", supplierRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("suppliers", supplierRepository.findAll());
        }
        model.addAttribute("search", search);
        return "ViewsSuppliers/Suppliers";
    }

    /**
     * Muestra el formulario para crear un nuevo proveedor o editar uno existente.
     * <p>
     * Este método maneja las solicitudes GET a la URL "/view/suppliers/form".
     * Si se proporciona un ID, se recupera el proveedor correspondiente para editarlo.
     * De lo contrario, se prepara un formulario para un nuevo proveedor.
     * </p>
     *
     * @param id El ID del proveedor a editar (opcional).
     * @param model El objeto {@link Model} para agregar el proveedor al formulario.
     * @return El nombre de la plantilla de vista para el formulario de proveedores.
     */
    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Suppliers supplier = supplierRepository.findById(id).orElse(new Suppliers());
            model.addAttribute("supplier", supplier);
        } else {
            model.addAttribute("supplier", new Suppliers());
        }
        return "ViewsSuppliers/Suppliers_form";
    }

    /**
     * Guarda o actualiza un proveedor en la base de datos.
     * <p>
     * Este método maneja las solicitudes POST a la URL "/view/suppliers/save".
     * Recibe un objeto {@link Suppliers} del formulario y lo guarda
     * usando el repositorio.
     * </p>
     *
     * @param supplier El objeto {@link Suppliers} a guardar.
     * @return Una redirección a la lista de proveedores.
     */
    @PostMapping("/save")
    public String save(@ModelAttribute Suppliers supplier) {
        supplierRepository.save(supplier);
        return "redirect:/view/suppliers";
    }

    /**
     * Muestra el formulario para editar un proveedor específico.
     * <p>
     * Este método maneja las solicitudes GET a la URL "/view/suppliers/edit/{id}".
     * Recupera el proveedor por su ID y lo añade al modelo para que el formulario
     * pueda ser precargado con sus datos.
     * </p>
     *
     * @param id El ID del proveedor a editar.
     * @param model El objeto {@link Model} para agregar el proveedor al formulario.
     * @return El nombre de la plantilla de vista para el formulario de proveedores.
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Suppliers supplier = supplierRepository.findById(id).orElse(new Suppliers());
        model.addAttribute("supplier", supplier);
        return "ViewsSuppliers/Suppliers_form";
    }

    /**
     * Elimina un proveedor de la base de datos por su ID.
     * <p>
     * Este método maneja las solicitudes POST a la URL "/view/suppliers/delete/{id}".
     * Elimina el proveedor y luego redirige a la lista de proveedores.
     * </p>
     *
     * @param id El ID del proveedor a eliminar.
     * @return Una redirección a la lista de proveedores.
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        supplierRepository.deleteById(id);
        return "redirect:/view/suppliers";
    }
}