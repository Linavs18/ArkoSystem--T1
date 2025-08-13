package co.edu.sena.arkosystem.repository;
import co.edu.sena.arkosystem.model.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface RepositoryClients extends JpaRepository<Clients,Long>{
    Optional<Clients> findByName(String name);

    List<Clients> findByNameContainingIgnoreCase(String name);
}
