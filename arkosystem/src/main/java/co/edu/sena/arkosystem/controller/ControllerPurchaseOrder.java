package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.PurchaseOrder;
import co.edu.sena.arkosystem.repository.RepositoryPurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class ControllerPurchaseOrder {
    @Autowired
    private RepositoryPurchaseOrder purchaseOrderRepository;

    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrder getPurchaseOrderById(@PathVariable Long id) {
        return purchaseOrderRepository.findById(id).orElse(null);
    }

    @PostMapping
    public PurchaseOrder createPurchaseOrder(@RequestBody PurchaseOrder order) {
        return purchaseOrderRepository.save(order);
    }

    @PutMapping("/{id}")
    public PurchaseOrder updatePurchaseOrder(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        order.setId(id);
        return purchaseOrderRepository.save(order);
    }

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
