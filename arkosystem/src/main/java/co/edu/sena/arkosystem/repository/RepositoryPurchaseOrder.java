package co.edu.sena.arkosystem.repository;
import co.edu.sena.arkosystem.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryPurchaseOrder extends JpaRepository<PurchaseOrder,Long>{
}
