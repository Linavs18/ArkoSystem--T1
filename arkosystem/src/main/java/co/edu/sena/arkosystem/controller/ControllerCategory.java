package co.edu.sena.arkosystem.controller;

import co.edu.sena.arkosystem.model.Category;
import co.edu.sena.arkosystem.repository.RepositoryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ControllerCategory {

    @Autowired
    private RepositoryCategory repositorycategory;

    @GetMapping
    public List<Category> getAllCategories() {
        return repositorycategory.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return repositorycategory.findById(id).orElse(null);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return repositorycategory.save(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return repositorycategory.save(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        repositorycategory.deleteById(id);
    }
}
