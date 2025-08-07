package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Inventario;
import co.edu.sena.arkosystem.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class ControllerInventario {
    @Autowired
    private InventarioRepository inventarioRepository;

    @GetMapping
    public List<Inventario> getAll() {
        return inventarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Inventario getByID(@PathVariable Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Inventario create(@RequestBody Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @PutMapping("/{id}")
    public Inventario update(@PathVariable Long id, @RequestBody Inventario inventario) {
        inventario.setId(id);
        return inventarioRepository.save(inventario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        inventarioRepository.deleteById(id);
    }
}
