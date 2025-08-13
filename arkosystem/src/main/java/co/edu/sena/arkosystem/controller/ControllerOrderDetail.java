package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.OrderDetails;
import co.edu.sena.arkosystem.repository.RepositoryOrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de los detalles de las órdenes.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los detalles de las órdenes. Las peticiones se mapean a la ruta base "/api/order-details".
 * </p>
 */
@RestController
@RequestMapping("/api/order-details")
public class ControllerOrderDetail {
    @Autowired
    private RepositoryOrderDetails orderDetailRepository;

    /**
     * Obtiene todos los detalles de las órdenes.
     * <p>
     * Responde a las peticiones GET a "/api/order-details".
     * </p>
     *
     * @return Una lista de todos los objetos {@link OrderDetails}.
     */
    @GetMapping
    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    /**
     * Obtiene un detalle de orden específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/order-details/{id}".
     * </p>
     *
     * @param id El ID del detalle de orden a buscar.
     * @return El objeto {@link OrderDetails} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public OrderDetails getOrderDetailById(@PathVariable Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo detalle de orden.
     * <p>
     * Responde a las peticiones POST a "/api/order-details". El detalle de la orden se envía en el cuerpo de la petición.
     * </p>
     *
     * @param orderDetail El objeto {@link OrderDetails} a crear.
     * @return El detalle de la orden guardado con su ID generado.
     */
    @PostMapping
    public OrderDetails createOrderDetail(@RequestBody OrderDetails orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    /**
     * Actualiza un detalle de orden existente.
     * <p>
     * Responde a las peticiones PUT a "/api/order-details/{id}". Se actualiza el detalle de la orden
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID del detalle de la orden a actualizar.
     * @param orderDetail El objeto {@link OrderDetails} con los datos actualizados.
     * @return El detalle de la orden actualizado.
     */
    @PutMapping("/{id}")
    public OrderDetails updateOrderDetail(@PathVariable Long id, @RequestBody OrderDetails orderDetail) {
        orderDetail.setId(id);
        return orderDetailRepository.save(orderDetail);
    }

    /**
     * Elimina un detalle de orden por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/order-details/{id}".
     * </p>
     *
     * @param id El ID del detalle de la orden a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteOrderDetail(@PathVariable Long id) {
        orderDetailRepository.deleteById(id);
    }
}