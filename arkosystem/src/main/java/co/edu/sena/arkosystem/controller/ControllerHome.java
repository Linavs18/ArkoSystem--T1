package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryClients;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controlador principal para la gestión de la página de inicio.
 * <p>
 * Esta clase maneja las solicitudes a la URL raíz "/" de la aplicación. Su función
 * principal es redirigir a los usuarios a la página apropiada (login, dashboard,
 * o la página de inicio del administrador) basándose en su estado de autenticación
 * y su rol.
 * </p>
 */
@Controller
public class ControllerHome {
    @Autowired
    private RepositoryInventory inventoryRepository;

    @Autowired
    private RepositoryClients clientsRepository;

    /**
     * Maneja la solicitud a la página de inicio.
     * <p>
     * Este método determina el flujo de navegación del usuario.
     * <ul>
     * <li>Si el usuario no está autenticado, se le redirige a la página de login.</li>
     * <li>Si el usuario tiene el rol "ROLE_CLIENT", se le redirige al dashboard.</li>
     * <li>Si el usuario tiene el rol "ROLE_ADMIN", se le carga la página de inicio
     * con estadísticas del sistema, como la cantidad de clientes, productos
     * y una lista de productos con bajo stock.</li>
     * <li>Para cualquier otro rol (ej. "ROLE_EMPLOYEE"), se le redirige a una página
     * específica para empleados.</li>
     * </ul>
     * </p>
     *
     * @param model El objeto {@link Model} para agregar datos a la vista.
     * @return El nombre de la vista a mostrar o la URL de redirección.
     */
    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si no está autenticado o es usuario anónimo, redirige al login
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }

        // Si está autenticado, redirige según el rol
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))) {
            return "redirect:/dashboard";
        } else if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("activePage", "home");
            List<Inventory> lowStockList = inventoryRepository.findLowStockItems();
            model.addAttribute("lowStockCount", lowStockList.size());
            model.addAttribute("lowStockList", lowStockList);
            model.addAttribute("clientCount", clientsRepository.count());
            model.addAttribute("productCount", inventoryRepository.count());
            return "index";
        } else {
            return "redirect:/employee";
        }
    }
}