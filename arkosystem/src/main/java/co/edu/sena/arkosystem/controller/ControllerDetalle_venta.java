package co.edu.sena.arkosystem.controller;


import co.edu.sena.arkosystem.model.Detalle_venta;
import co.edu.sena.arkosystem.repository.Detalle_ventaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-venta")
public class ControllerDetalle_venta {

    @Autowired
    private Detalle_ventaRepository detalleVentaRepository;

    @GetMapping
    public List<Detalle_venta> getAll() {
        return detalleVentaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Detalle_venta getByID(@PathVariable Long id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Detalle_venta create(@RequestBody Detalle_venta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @PutMapping("/{id}")
    public Detalle_venta update(@PathVariable Long id, @RequestBody Detalle_venta detalleVenta) {
        detalleVenta.setId(id);
        return detalleVentaRepository.save(detalleVenta);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        detalleVentaRepository.deleteById(id);
    }
}
