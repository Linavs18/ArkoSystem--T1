package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Roles;
import co.edu.sena.arkosystem.repository.RepositoryRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de roles de usuario.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los roles de usuario. Las peticiones se mapean a la ruta base "/api/roles".
 * </p>
 */
@RestController
@RequestMapping("/api/roles")
public class ControllerRoles {

    @Autowired
    private RepositoryRole rolesRepository;

    /**
     * Obtiene todos los roles de la base de datos.
     * <p>
     * Responde a las peticiones GET a "/api/roles".
     * </p>
     *
     * @return Una lista de todos los objetos {@link Roles}.
     */
    @GetMapping
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    /**
     * Obtiene un rol específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/roles/{id}".
     * </p>
     *
     * @param id El ID del rol a buscar.
     * @return El objeto {@link Roles} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public Roles getRoleById(@PathVariable Long id) {
        return rolesRepository.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo rol.
     * <p>
     * Responde a las peticiones POST a "/api/roles". El rol se envía en el cuerpo de la petición.
     * </p>
     *
     * @param role El objeto {@link Roles} a crear.
     * @return El rol guardado con su ID generado.
     */
    @PostMapping
    public Roles createRole(@RequestBody Roles role) {
        return rolesRepository.save(role);
    }

    /**
     * Actualiza un rol existente.
     * <p>
     * Responde a las peticiones PUT a "/api/roles/{id}". Se actualiza el rol
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID del rol a actualizar.
     * @param role El objeto {@link Roles} con los datos actualizados.
     * @return El rol actualizado.
     */
    @PutMapping("/{id}")
    public Roles updateRole(@PathVariable Long id, @RequestBody Roles role) {
        role.setId(id);
        return rolesRepository.save(role);
    }

    /**
     * Elimina un rol por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/roles/{id}".
     * </p>
     *
     * @param id El ID del rol a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        rolesRepository.deleteById(id);
    }
}