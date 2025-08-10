package co.edu.sena.arkosystem.repository;
import co.edu.sena.arkosystem.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryInventory extends JpaRepository <Inventory,Long>{
    @Query("SELECT i FROM Inventory i WHERE i.availableQuantity <= i.minStock")
    List<Inventory> findLowStockItems();

    List<Inventory> findByCategory_Id(Long categoryId);
}
