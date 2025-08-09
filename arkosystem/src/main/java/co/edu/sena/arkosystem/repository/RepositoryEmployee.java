package co.edu.sena.arkosystem.repository;
import co.edu.sena.arkosystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEmployee extends JpaRepository <Employee, Long>{
}
