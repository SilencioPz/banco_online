package source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import source.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByUsername(String username);
}