package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.SaleDetails;
import co.edu.sena.arkosystem.repository.RepositorySaleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de los detalles de las ventas.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los detalles de las ventas. Las peticiones se mapean a la ruta base "/api/sale-details".
 * </p>
 */
@RestController
@RequestMapping("/api/sale-details")
public class ControllerSaleDetail {
    @Autowired
    private RepositorySaleDetails saleDetailRepository;

    /**
     * Obtiene todos los detalles de las ventas.
     * <p>
     * Responde a las peticiones GET a "/api/sale-details".
     * </p>
     *
     * @return Una lista de todos los objetos {@link SaleDetails}.
     */
    @GetMapping
    public List<SaleDetails> getAllSaleDetails() {
        return saleDetailRepository.findAll();
    }

    /**
     * Obtiene un detalle de venta específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/sale-details/{id}".
     * </p>
     *
     * @param id El ID del detalle de venta a buscar.
     * @return El objeto {@link SaleDetails} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public SaleDetails getSaleDetailById(@PathVariable Long id) {
        return saleDetailRepository.findById(id).orElse(null);
    }



    /**
     * Crea un nuevo detalle de venta.
     * <p>
     * Responde a las peticiones POST a "/api/sale-details". El detalle de la venta se envía en el cuerpo de la petición.
     * </p>
     *
     * @param saleDetail El objeto {@link SaleDetails} a crear.
     * @return El detalle de la venta guardado con su ID generado.
     */
    @PostMapping
    public SaleDetails createSaleDetail(@RequestBody SaleDetails saleDetail) {
        return saleDetailRepository.save(saleDetail);
    }

    /**
     * Actualiza un detalle de venta existente.
     * <p>
     * Responde a las peticiones PUT a "/api/sale-details/{id}". Se actualiza el detalle de la venta
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID del detalle de la venta a actualizar.
     * @param saleDetail El objeto {@link SaleDetails} con los datos actualizados.
     * @return El detalle de la venta actualizado.
     */
    @PutMapping("/{id}")
    public SaleDetails updateSaleDetail(@PathVariable Long id, @RequestBody SaleDetails saleDetail) {
        saleDetail.setId(id);
        return saleDetailRepository.save(saleDetail);
    }

    /**
     * Elimina un detalle de venta por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/sale-details/{id}".
     * </p>
     *
     * @param id El ID del detalle de la venta a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteSaleDetail(@PathVariable Long id) {
        saleDetailRepository.deleteById(id);
    }
}
