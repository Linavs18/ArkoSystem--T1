package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.PurchaseOrder;
import co.edu.sena.arkosystem.repository.RepositoryPurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de órdenes de compra.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre las órdenes de compra. Las peticiones se mapean a la ruta base "/api/purchase-orders".
 * </p>
 */
@RestController
@RequestMapping("/api/purchase-orders")
public class ControllerPurchaseOrder {
    @Autowired
    private RepositoryPurchaseOrder purchaseOrderRepository;

    /**
     * Obtiene todas las órdenes de compra de la base de datos.
     * <p>
     * Responde a las peticiones GET a "/api/purchase-orders".
     * </p>
     *
     * @return Una lista de todos los objetos {@link PurchaseOrder}.
     */
    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    /**
     * Obtiene una orden de compra específica por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/purchase-orders/{id}".
     * </p>
     *
     * @param id El ID de la orden de compra a buscar.
     * @return El objeto {@link PurchaseOrder} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public PurchaseOrder getPurchaseOrderById(@PathVariable Long id) {
        return purchaseOrderRepository.findById(id).orElse(null);
    }

    /**
     * Crea una nueva orden de compra.
     * <p>
     * Responde a las peticiones POST a "/api/purchase-orders". La orden se envía en el cuerpo de la petición.
     * </p>
     *
     * @param order El objeto {@link PurchaseOrder} a crear.
     * @return La orden de compra guardada con su ID generado.
     */
    @PostMapping
    public PurchaseOrder createPurchaseOrder(@RequestBody PurchaseOrder order) {
        return purchaseOrderRepository.save(order);
    }

    /**
     * Actualiza una orden de compra existente.
     * <p>
     * Responde a las peticiones PUT a "/api/purchase-orders/{id}". Se actualiza la orden de compra
     * con el ID especificado en la URL.
     * </p>
     *
     * @param id El ID de la orden de compra a actualizar.
     * @param order El objeto {@link PurchaseOrder} con los datos actualizados.
     * @return La orden de compra actualizada.
     */
    @PutMapping("/{id}")
    public PurchaseOrder updatePurchaseOrder(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        order.setId(id);
        return purchaseOrderRepository.save(order);
    }

    /**
     * Actualiza el estado de una orden de compra.
     * <p>
     * Responde a las peticiones PUT a "/api/purchase-orders/{id}/status/{status}".
     * </p>
     *
     * @param id El ID de la orden de compra a actualizar.
     * @param status El nuevo estado de la orden.
     * @return La orden de compra actualizada o {@code null} si no se encuentra.
     */
    @PutMapping("/{id}/status/{status}")
    public PurchaseOrder updateOrderStatus(@PathVariable Long id, @PathVariable String status) {
        PurchaseOrder order = purchaseOrderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            return purchaseOrderRepository.save(order);
        }
        return null;
    }
}