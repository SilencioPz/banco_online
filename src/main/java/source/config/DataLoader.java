package source.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import source.model.Cliente;
import source.repository.ClienteRepository;
import source.model.Cliente.Role;

@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    public DataLoader(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Cliente admin = clienteRepository.findById(1L).orElse(null);
        if (admin != null && admin.getRole() != Role.ROLE_ADMIN) {
            admin.setRole(Role.ROLE_ADMIN);
            clienteRepository.save(admin);
        }
    }
}