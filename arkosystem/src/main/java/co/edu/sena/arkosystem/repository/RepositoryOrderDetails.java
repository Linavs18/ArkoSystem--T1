package co.edu.sena.arkosystem.repository;
import co.edu.sena.arkosystem.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryOrderDetails extends JpaRepository<OrderDetails, Long>{
}
