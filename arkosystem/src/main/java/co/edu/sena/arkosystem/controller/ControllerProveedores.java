package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Proveedores;
import co.edu.sena.arkosystem.repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
public class ControllerProveedores {
    @Autowired
    private ProveedoresRepository proveedoresRepository;

    @GetMapping
    public List<Proveedores> getAll() {
        return proveedoresRepository.findAll();
    }

    @GetMapping("/{id}")
    public Proveedores getByID(@PathVariable Long id) {
        return proveedoresRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Proveedores create(@RequestBody Proveedores proveedores) {
        return proveedoresRepository.save(proveedores);
    }

    @PutMapping("/{id}")
    public Proveedores update(@PathVariable Long id, @RequestBody Proveedores proveedores) {
        proveedores.setId(id);
        return proveedoresRepository.save(proveedores);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        proveedoresRepository.deleteById(id);
    }
}
