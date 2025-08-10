package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Clients;
import co.edu.sena.arkosystem.repository.RepositoryClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ControllerClients {

    @Autowired
    private RepositoryClients repositoryclients;

    @GetMapping
    public List<Clients> getAllClients() {
        return repositoryclients.findAll();
    }

    @GetMapping("/{id}")
    public Clients getClientById(@PathVariable Long id) {
        return repositoryclients.findById(id).orElse(null);
    }

    @PostMapping
    public Clients createClient(@RequestBody Clients client) {
        return repositoryclients.save(client);
    }

    @PutMapping("/{id}")
    public Clients updateClient(@PathVariable Long id, @RequestBody Clients client) {
        client.setId(id);
        return repositoryclients.save(client);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        repositoryclients.deleteById(id);
    }
}
