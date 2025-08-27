package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Suppliers;
import co.edu.sena.arkosystem.repository.RepositorySuppliers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de proveedores.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los proveedores. Las peticiones se mapean a la ruta base "/api/suppliers".
 * </p>
 */
@RestController
@RequestMapping("/api/suppliers")
public class ControllerSupplier {
    @Autowired
    private RepositorySuppliers supplierRepository;

    /**
     * Obtiene todos los proveedores de la base de datos.
     * <p>
     * Responde a las peticiones GET a "/api/suppliers".
     * </p>
     *
     * @return Una lista de todos los objetos {@link Suppliers}.
     */
    @GetMapping
    public List<Suppliers> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    /**
     * Obtiene un proveedor específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/suppliers/{id}".
     * </p>
     *
     * @param id El ID del proveedor a buscar.
     * @return El objeto {@link Suppliers} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public Suppliers getSupplierById(@PathVariable Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo proveedor.
     * <p>
     * Responde a las peticiones POST a "/api/suppliers". El proveedor se envía en el cuerpo de la petición.
     * </p>
     *
     * @param supplier El objeto {@link Suppliers} a crear.
     * @return El proveedor guardado con su ID generado.
     */
    @PostMapping
    public Suppliers createSupplier(@RequestBody Suppliers supplier) {
        return supplierRepository.save(supplier);
    }

    /**
     * Actualiza un proveedor existente.
     * <p>
     * Responde a las peticiones PUT a "/api/suppliers/{id}". Se actualiza el proveedor
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID del proveedor a actualizar.
     * @param supplier El objeto {@link Suppliers} con los datos actualizados.
     * @return El proveedor actualizado.
     */
    @PutMapping("/{id}")
    public Suppliers updateSupplier(@PathVariable Long id, @RequestBody Suppliers supplier) {
        supplier.setId(id);
        return supplierRepository.save(supplier);
    }

    /**
     * Elimina un proveedor por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/suppliers/{id}".
     * </p>
     *
     * @param id El ID del proveedor a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierRepository.deleteById(id);
    }
}
