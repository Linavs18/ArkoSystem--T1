package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Employee;
import co.edu.sena.arkosystem.repository.RepositoryEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class ControllerEmployee {
    @Autowired
    private RepositoryEmployee repositoryemployee;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return repositoryemployee.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return repositoryemployee.findById(id).orElse(null);
    }

    @GetMapping("/document/{document}")
    public Employee getEmployeeByDocument(@PathVariable long document) {
        return repositoryemployee.findByDocument(document);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return repositoryemployee.save(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return repositoryemployee.save(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repositoryemployee.deleteById(id);
    }
}
