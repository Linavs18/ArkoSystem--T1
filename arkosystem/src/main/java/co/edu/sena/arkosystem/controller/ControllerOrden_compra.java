package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Orden_compra;
import co.edu.sena.arkosystem.repository.Detalle_compraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orden-compra")
public class ControllerOrden_compra {

    @Autowired
    private Detalle_compraRepository ordenCompraRepository;

    @GetMapping
    public List<Orden_compra> getAll() {
        return ordenCompraRepository.findAll();
    }

    @GetMapping("/{id}")
    public Orden_compra getByID(@PathVariable Long id) {
        return ordenCompraRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Orden_compra create(@RequestBody Orden_compra ordenCompra) {
        return ordenCompraRepository.save(ordenCompra);
    }

    @PutMapping("/{id}")
    public Orden_compra update(@PathVariable Long id, @RequestBody Orden_compra ordenCompra) {
        ordenCompra.setId(id);
        return ordenCompraRepository.save(ordenCompra);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ordenCompraRepository.deleteById(id);
    }
}
