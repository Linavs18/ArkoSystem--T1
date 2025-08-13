package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Users;
import co.edu.sena.arkosystem.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los usuarios. Las peticiones se mapean a la ruta base "/api/users".
 * </p>
 */
@RestController
@RequestMapping("/api/users")
public class ControllerUsers {

    @Autowired
    private RepositoryUser userRepository;

    /**
     * Obtiene todos los usuarios de la base de datos.
     * <p>
     * Responde a las peticiones GET a "/api/users".
     * </p>
     *
     * @return Una lista de todos los objetos {@link Users}.
     */
    @GetMapping
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Obtiene un usuario específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/users/{id}".
     * </p>
     *
     * @param id El ID del usuario a buscar.
     * @return El objeto {@link Users} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo usuario.
     * <p>
     * Responde a las peticiones POST a "/api/users". El usuario se envía en el cuerpo de la petición.
     * </p>
     *
     * @param user El objeto {@link Users} a crear.
     * @return El usuario guardado con su ID generado.
     */
    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userRepository.save(user);
    }

    /**
     * Actualiza un usuario existente.
     * <p>
     * Responde a las peticiones PUT a "/api/users/{id}". Se actualiza el usuario
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID del usuario a actualizar.
     * @param user El objeto {@link Users} con los datos actualizados.
     * @return El usuario actualizado.
     */
    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users user) {
        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * Elimina un usuario por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/users/{id}".
     * </p>
     *
     * @param id El ID del usuario a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
