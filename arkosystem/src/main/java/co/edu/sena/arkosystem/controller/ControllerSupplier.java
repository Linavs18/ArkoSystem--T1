package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Suppliers;
import co.edu.sena.arkosystem.repository.RepositorySuppliers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class ControllerSupplier {
    @Autowired
    private RepositorySuppliers supplierRepository;

    @GetMapping
    public List<Suppliers> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @GetMapping("/{id}")
    public Suppliers getSupplierById(@PathVariable Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Suppliers createSupplier(@RequestBody Suppliers supplier) {
        return supplierRepository.save(supplier);
    }

    @PutMapping("/{id}")
    public Suppliers updateSupplier(@PathVariable Long id, @RequestBody Suppliers supplier) {
        supplier.setId(id);
        return supplierRepository.save(supplier);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierRepository.deleteById(id);
    }
}
