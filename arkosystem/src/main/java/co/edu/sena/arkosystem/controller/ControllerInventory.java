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

/**
 * Controlador REST para la gestión del inventario.
 * <p>
 * Esta clase proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los productos del inventario. También maneja la subida de imágenes para los productos y
 * proporciona un endpoint para obtener productos con bajo stock.
 * </p>
 */
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

    /**
     * Obtiene todos los productos del inventario.
     * <p>
     * Responde a las peticiones GET a "/api/inventory".
     * </p>
     *
     * @return Una lista de todos los objetos {@link Inventory}.
     */
    @GetMapping
    public List<Inventory> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    /**
     * Obtiene un producto del inventario específico por su ID.
     * <p>
     * Responde a las peticiones GET a "/api/inventory/{id}".
     * </p>
     *
     * @param id El ID del producto a buscar.
     * @return El objeto {@link Inventory} si se encuentra, o {@code null} si no.
     */
    @GetMapping("/{id}")
    public Inventory getInventoryItemById(@PathVariable Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }


    /**
     * Crea un nuevo producto en el inventario.
     * <p>
     * Responde a las peticiones POST a "/api/inventory". Permite la subida de una imagen
     * para el producto. Si no se proporciona una imagen, se utiliza una imagen por defecto.
     * </p>
     *
     * @param name El nombre del producto.
     * @param price El precio del producto.
     * @param categoryId El ID de la categoría del producto.
     * @param availableQuantity La cantidad disponible del producto.
     * @param minStock El stock mínimo del producto (opcional, por defecto es 5).
     * @param supplierId El ID del proveedor del producto.
     * @param image El archivo de imagen del producto (opcional).
     * @return El producto guardado con su ID generado.
     * @throws IOException Si ocurre un error durante la subida del archivo de imagen.
     */
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

        // Se asigna la categoría y el proveedor
        inventory.setCategory(categoryRepository.findById(categoryId).orElse(null));
        inventory.setSupplier(supplierRepository.findById(supplierId).orElse(null));

        // Manejo de la imagen opcional
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

    /**
     * Actualiza un producto existente en el inventario.
     * <p>
     * Responde a las peticiones PUT a "/api/inventory/{id}". Permite la subida de una nueva imagen
     * para el producto.
     * </p>
     *
     * @param id El ID del producto a actualizar.
     * @param name El nombre actualizado del producto.
     * @param price El precio actualizado del producto.
     * @param categoryId El ID de la categoría actualizada del producto.
     * @param availableQuantity La cantidad disponible actualizada del producto.
     * @param minStock El stock mínimo actualizado del producto (opcional).
     * @param supplierId El ID del proveedor actualizado del producto.
     * @param image El nuevo archivo de imagen del producto (opcional).
     * @return El producto actualizado.
     * @throws IOException Si ocurre un error durante la subida del archivo de imagen.
     */
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

        // Se actualiza la categoría y el proveedor
        inventory.setCategory(categoryRepository.findById(categoryId).orElse(null));
        inventory.setSupplier(supplierRepository.findById(supplierId).orElse(null));

        // Manejo de la actualización de la imagen
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

    /**
     * Elimina un producto del inventario por su ID.
     * <p>
     * Responde a las peticiones DELETE a "/api/inventory/{id}".
     * </p>
     *
     * @param id El ID del producto a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteInventoryItem(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
    }

    /**
     * Obtiene una lista de productos con bajo stock.
     * <p>
     * Responde a las peticiones GET a "/api/inventory/low-stock".
     * </p>
     *
     * @return Una lista de objetos {@link Inventory} con bajo stock.
     */
    @GetMapping("/low-stock")
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }
}