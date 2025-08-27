package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Sale;
import co.edu.sena.arkosystem.repository.RepositorySale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de ventas.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre las ventas. Las peticiones se mapean a la ruta base "/api/sales".
 * </p>
 */
@RestController
@RequestMapping("/api/sales")
public class ControllerSale {
    @Autowired
    private RepositorySale saleRepository;

    /**
     * Obtiene todas las ventas de la base de datos.
     * <p>
     * Responde a las peticiones GET a "/api/sales".
     * </p>
     *
     * @return Una lista de todos los objetos {@link Sale}.
     */
    @GetMapping
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    /**
     * Obtiene una venta específica por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/sales/{id}".
     * </p>
     *
     * @param id El ID de la venta a buscar.
     * @return El objeto {@link Sale} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public Sale getSaleById(@PathVariable Long id) {
        return saleRepository.findById(id).orElse(null);
    }



    /**
     * Crea una nueva venta.
     * <p>
     * Responde a las peticiones POST a "/api/sales". La venta se envía en el cuerpo de la petición.
     * </p>
     *
     * @param sale El objeto {@link Sale} a crear.
     * @return La venta guardada con su ID generado.
     */
    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleRepository.save(sale);
    }

    /**
     * Actualiza una venta existente.
     * <p>
     * Responde a las peticiones PUT a "/api/sales/{id}". Se actualiza la venta
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID de la venta a actualizar.
     * @param sale El objeto {@link Sale} con los datos actualizados.
     * @return La venta actualizada.
     */
    @PutMapping("/{id}")
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        sale.setId(id);
        return saleRepository.save(sale);
    }

    /**
     * Actualiza el estado de una venta.
     * <p>
     * Responde a las peticiones PUT a "/api/sales/{id}/status/{status}".
     * </p>
     *
     * @param id El ID de la venta a actualizar.
     * @param status El nuevo estado de la venta.
     * @return La venta actualizada o {@code null} si no se encuentra.
     */
    @PutMapping("/{id}/status/{status}")
    public Sale updateSaleStatus(@PathVariable Long id, @PathVariable String status) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if (sale != null) {
            sale.setStatus(status);
            return saleRepository.save(sale);
        }
        return null;
    }

    /**
     * Elimina una venta por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/sales/{id}".
     * </p>
     *
     * @param id El ID de la venta a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteSale(@PathVariable Long id) {
        saleRepository.deleteById(id);
    }
}
