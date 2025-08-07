package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Venta;
import co.edu.sena.arkosystem.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venta")
public class ControllerVenta {
    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping
    public List<Venta> getAll() {
        return ventaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Venta getByID(@PathVariable Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Venta create(@RequestBody Venta venta) {
        return ventaRepository.save(venta);
    }

    @PutMapping("/{id}")
    public Venta update(@PathVariable Long id, @RequestBody Venta venta) {
        venta.setId(id);
        return ventaRepository.save(venta);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ventaRepository.deleteById(id);
    }
}
