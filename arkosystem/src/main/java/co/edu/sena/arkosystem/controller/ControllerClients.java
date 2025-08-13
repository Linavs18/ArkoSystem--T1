package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Clients;
import co.edu.sena.arkosystem.repository.RepositoryClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de clientes.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los clientes. Las peticiones se mapean a la ruta base "/api/clients".
 * </p>
 */
@RestController
@RequestMapping("/api/clients")
public class ControllerClients {

    @Autowired
    private RepositoryClients repositoryclients;

    /**
     * Obtiene todos los clientes de la base de datos.
     * <p>
     * Responde a las peticiones GET a "/api/clients".
     * </p>
     *
     * @return Una lista de todos los objetos {@link Clients}.
     */
    @GetMapping
    public List<Clients> getAllClients() {
        return repositoryclients.findAll();
    }

    /**
     * Obtiene un cliente específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/clients/{id}".
     * </p>
     *
     * @param id El ID del cliente a buscar.
     * @return El objeto {@link Clients} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public Clients getClientById(@PathVariable Long id) {
        return repositoryclients.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo cliente.
     * <p>
     * Responde a las peticiones POST a "/api/clients". El cliente se envía en el cuerpo de la petición.
     * </p>
     *
     * @param client El objeto {@link Clients} a crear.
     * @return El cliente guardado con su ID generado.
     */
    @PostMapping
    public Clients createClient(@RequestBody Clients client) {
        return repositoryclients.save(client);
    }

    /**
     * Actualiza un cliente existente.
     * <p>
     * Responde a las peticiones PUT a "/api/clients/{id}". Se actualiza el cliente
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID del cliente a actualizar.
     * @param client El objeto {@link Clients} con los datos actualizados.
     * @return El cliente actualizado.
     */
    @PutMapping("/{id}")
    public Clients updateClient(@PathVariable Long id, @RequestBody Clients client) {
        client.setId(id);
        return repositoryclients.save(client);
    }

    /**
     * Elimina un cliente por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/clients/{id}".
     * </p>
     *
     * @param id El ID del cliente a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        repositoryclients.deleteById(id);
    }
}