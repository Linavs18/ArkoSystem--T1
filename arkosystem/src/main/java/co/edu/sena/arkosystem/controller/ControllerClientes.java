package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Clientes;
import co.edu.sena.arkosystem.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ControllerClientes {
    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping
    public List<Clientes> getAll() {
        return clientesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Clientes getByID(@PathVariable Long id) {
        return clientesRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Clientes create(@RequestBody Clientes clientes) {
        return clientesRepository.save(clientes);
    }

    @PutMapping("/{id}")
    public Clientes update(@PathVariable Long id, @RequestBody Clientes clientes) {
        clientes.setId(id);
        return clientesRepository.save(clientes);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientesRepository.deleteById(id);
    }
}
