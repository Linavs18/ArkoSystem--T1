package co.edu.sena.arkosystem.repository;
import co.edu.sena.arkosystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RepositoryEmployee extends JpaRepository <Employee, Long>{
    @Query("SELECT e FROM Employee e WHERE e.document = :document")
    Employee findByDocument(@Param("document") long document);
    Optional<Employee> findByUserId(Long userId);

    List<Employee> findByNameContainingIgnoreCase(String name);
}
