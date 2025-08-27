package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Employee;
import co.edu.sena.arkosystem.repository.RepositoryEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de empleados.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los empleados. Las peticiones se mapean a la ruta base "/api/employees".
 * </p>
 */
@RestController
@RequestMapping("/api/employees")
public class ControllerEmployee {
    @Autowired
    private RepositoryEmployee repositoryemployee;

    /**
     * Obtiene todos los empleados de la base de datos.
     * <p>
     * Responde a las peticiones GET a "/api/employees".
     * </p>
     *
     * @return Una lista de todos los objetos {@link Employee}.
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        return repositoryemployee.findAll();
    }

    /**
     * Obtiene un empleado específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/employees/{id}".
     * </p>
     *
     * @param id El ID del empleado a buscar.
     * @return El objeto {@link Employee} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return repositoryemployee.findById(id).orElse(null);
    }

    /**
     * Obtiene un empleado específico por su número de documento.
     * <p>
     * Responde a las peticiones GET a "/api/employees/document/{document}".
     * </p>
     *
     * @param document El número de documento del empleado a buscar.
     * @return El objeto {@link Employee} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/document/{document}")
    public Employee getEmployeeByDocument(@PathVariable long document) {
        return repositoryemployee.findByDocument(document);
    }

    /**
     * Crea un nuevo empleado.
     * <p>
     * Responde a las peticiones POST a "/api/employees". El empleado se envía en el cuerpo de la petición.
     * </p>
     *
     * @param employee El objeto {@link Employee} a crear.
     * @return El empleado guardado con su ID generado.
     */
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return repositoryemployee.save(employee);
    }

    /**
     * Actualiza un empleado existente.
     * <p>
     * Responde a las peticiones PUT a "/api/employees/{id}". Se actualiza el empleado
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID del empleado a actualizar.
     * @param employee El objeto {@link Employee} con los datos actualizados.
     * @return El empleado actualizado.
     */
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return repositoryemployee.save(employee);
    }

    /**
     * Elimina un empleado por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/employees/{id}".
     * </p>
     *
     * @param id El ID del empleado a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repositoryemployee.deleteById(id);
    }
}