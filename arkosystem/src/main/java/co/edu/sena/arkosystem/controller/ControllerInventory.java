package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class ControllerInventory {
    @Autowired
    private RepositoryInventory inventoryRepository;

    @GetMapping
    public List<Inventory> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Inventory getInventoryItemById(@PathVariable Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }


    @PostMapping
    public Inventory createInventoryItem(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @PutMapping("/{id}")
    public Inventory updateInventoryItem(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id);
        return inventoryRepository.save(inventory);
    }

    @DeleteMapping("/{id}")
    public void deleteInventoryItem(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
    }
}
