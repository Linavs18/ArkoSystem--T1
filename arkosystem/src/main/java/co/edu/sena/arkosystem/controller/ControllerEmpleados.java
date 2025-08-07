package co.edu.sena.arkosystem.controller;


import co.edu.sena.arkosystem.model.Empleados;
import co.edu.sena.arkosystem.repository.EmpleadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados")
public class ControllerEmpleados {
    @Autowired
    private EmpleadosRepository empleadosRepository;

    @GetMapping
    public List<Empleados> getAll() {
        return empleadosRepository.findAll();
    }

    @GetMapping("/{id}")
    public Empleados getByID(@PathVariable Long id) {
        return empleadosRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Empleados create(@RequestBody Empleados empleados) {
        return empleadosRepository.save(empleados);
    }

    @PutMapping("/{id}")
    public Empleados update(@PathVariable Long id, @RequestBody Empleados empleados) {
        empleados.setId(id);
        return empleadosRepository.save(empleados);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        empleadosRepository.deleteById(id);
    }
}
