package co.edu.sena.arkosystem.repository;
import co.edu.sena.arkosystem.model.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorySuppliers extends JpaRepository<Suppliers,Long>{
    List<Suppliers> findByNameContainingIgnoreCase(String name);
}
