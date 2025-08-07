package co.edu.sena.arkosystem.repository;

import co.edu.sena.arkosystem.model.Orden_compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Detalle_compraRepository  extends JpaRepository<Orden_compra, Long> {
}
