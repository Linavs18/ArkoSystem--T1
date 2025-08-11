package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Roles;
import co.edu.sena.arkosystem.repository.RepositoryRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class ControllerRoles {

    @Autowired
    private RepositoryRoles rolesRepository;

    @GetMapping
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Roles getRoleById(@PathVariable Long id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Roles createRole(@RequestBody Roles role) {
        return rolesRepository.save(role);
    }

    @PutMapping("/{id}")
    public Roles updateRole(@PathVariable Long id, @RequestBody Roles role) {
        role.setId(id);
        return rolesRepository.save(role);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        rolesRepository.deleteById(id);
    }
}
