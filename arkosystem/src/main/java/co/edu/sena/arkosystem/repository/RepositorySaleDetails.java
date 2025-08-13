package co.edu.sena.arkosystem.repository;

import co.edu.sena.arkosystem.model.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RepositorySaleDetails extends JpaRepository<SaleDetails, Long> {
    List<SaleDetails> findBySale_Id(Long saleId);
}
