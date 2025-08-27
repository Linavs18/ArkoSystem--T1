package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

/**
 * Controlador para la visualización del panel de control (dashboard).
 * <p>
 * Esta clase maneja las solicitudes relacionadas con la página principal del dashboard
 * y la información que se muestra en ella, como el inventario de productos.
 * </p>
 */
@Controller
public class ControllerDashboard {

    @Autowired
    private RepositoryInventory inventoryRepository;

    /**
     * Muestra la página principal del dashboard con la lista de productos del inventario.
     * <p>
     * Este método recupera todos los productos del inventario de la base de datos
     * y los agrega al modelo para que puedan ser visualizados en la plantilla del dashboard.
     * </p>
     *
     * @param model El objeto {@link Model} para agregar los datos a la vista.
     * @return El nombre de la vista de la página del dashboard.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Inventory> productos = inventoryRepository.findAll();
        model.addAttribute("productos", productos);
        return "dashboard";
    }
}