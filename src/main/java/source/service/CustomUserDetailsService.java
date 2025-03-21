package source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import source.repository.ClienteRepository;
import source.model.Cliente;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    public CustomUserDetailsService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Cliente> clienteOptional = Optional.ofNullable(clienteRepository.findByUsername(username));

        Cliente cliente = clienteOptional.orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado")
        );

        return new User(
                cliente.getUsername(),
                cliente.getPasswordHash(),
                cliente.isEnabled(),
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority(cliente.getRole().name()))
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Cliente.Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}