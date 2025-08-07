package co.edu.sena.arkosystem.controller;


import co.edu.sena.arkosystem.model.Detalle_orden;
import co.edu.sena.arkosystem.repository.Detalle_ordenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle_orden")
public class ControllerDetalle_orden {
    @Autowired
    private Detalle_ordenRepository detalleOrdenRepository;

    @GetMapping
    public List<Detalle_orden> getAll() {
        return detalleOrdenRepository.findAll();
    }

    @GetMapping("/{id}")
    public Detalle_orden getByID(@PathVariable Long id) {
        return detalleOrdenRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Detalle_orden create(@RequestBody Detalle_orden detalleOrden) {
        return detalleOrdenRepository.save(detalleOrden);
    }

    @PutMapping("/{id}")
    public Detalle_orden update(@PathVariable Long id, @RequestBody Detalle_orden detalleOrden) {
        detalleOrden.setId(id);
        return detalleOrdenRepository.save(detalleOrden);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        detalleOrdenRepository.deleteById(id);
    }
}
