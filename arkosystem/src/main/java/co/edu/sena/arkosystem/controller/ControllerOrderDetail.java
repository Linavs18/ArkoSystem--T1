package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.OrderDetails;
import co.edu.sena.arkosystem.repository.RepositoryOrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class ControllerOrderDetail {
    @Autowired
    private RepositoryOrderDetails orderDetailRepository;

    @GetMapping
    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @GetMapping("/{id}")
    public OrderDetails getOrderDetailById(@PathVariable Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @PostMapping
    public OrderDetails createOrderDetail(@RequestBody OrderDetails orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @PutMapping("/{id}")
    public OrderDetails updateOrderDetail(@PathVariable Long id, @RequestBody OrderDetails orderDetail) {
        orderDetail.setId(id);
        return orderDetailRepository.save(orderDetail);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderDetail(@PathVariable Long id) {
        orderDetailRepository.deleteById(id);
    }
}
