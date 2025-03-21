package source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.repository.ClienteRepository;
import source.model.Cliente;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findByUsername(String username) {
        return clienteRepository.findByUsername(username);
    }
}