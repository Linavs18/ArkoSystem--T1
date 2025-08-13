package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Inventory;
import co.edu.sena.arkosystem.repository.RepositoryCategory;
import co.edu.sena.arkosystem.repository.RepositoryInventory;
import co.edu.sena.arkosystem.repository.RepositorySuppliers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class ControllerInventory {
    @Autowired
    private RepositoryInventory inventoryRepository;

    @Autowired
    private RepositoryCategory categoryRepository;

    @Autowired
    private RepositorySuppliers supplierRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/uploads/";

    private static final String DEFAULT_IMAGE = "/assets/img/uploads/Caja_vacia.jpg";

    @GetMapping
    public List<Inventory> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Inventory getInventoryItemById(@PathVariable Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }


    @PostMapping
    public Inventory createInventoryItem(@RequestParam("name") String name,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("category_id") Long categoryId,
                                         @RequestParam("available_quantity") Integer availableQuantity,
                                         @RequestParam(value = "min_stock", required = false) Integer minStock,
                                         @RequestParam("supplier_id") Long supplierId,
                                         @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Inventory inventory = new Inventory();
        inventory.setName(name);
        inventory.setPrice(price);
        inventory.setAvailableQuantity(availableQuantity);
        inventory.setMinStock(minStock != null ? minStock : 5);

        // Imagen opcional
        if (image != null && !image.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, image.getBytes());

            inventory.setImageUrl("/assets/img/uploads/" + fileName); // URL accesible
        } else {
            inventory.setImageUrl(DEFAULT_IMAGE); // Imagen por defecto
        }

        return inventoryRepository.save(inventory);
    }

    @PutMapping("/{id}")
    public Inventory updateInventoryItem(@PathVariable Long id,
                                         @RequestParam("name") String name,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("category_id") Long categoryId,
                                         @RequestParam("available_quantity") Integer availableQuantity,
                                         @RequestParam(value = "min_stock", required = false) Integer minStock,
                                         @RequestParam("supplier_id") Long supplierId,
                                         @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Inventory inventory = inventoryRepository.findById(id).orElse(null);
        if (inventory == null) return null;

        inventory.setName(name);
        inventory.setPrice(price);
        inventory.setAvailableQuantity(availableQuantity);
        inventory.setMinStock(minStock != null ? minStock : inventory.getMinStock());

        if (image != null && !image.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, image.getBytes());

            inventory.setImageUrl("/assets/img/uploads/" + fileName);
        }

        return inventoryRepository.save(inventory);
    }

    @DeleteMapping("/{id}")
    public void deleteInventoryItem(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
    }

    @GetMapping("/low-stock")
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }
}
