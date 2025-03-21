package source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.repository.AccountRepository;
import source.repository.ClienteRepository;
import source.model.Account;
import source.model.Cliente;

import java.math.BigDecimal;

@Controller
public class CadastroController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AccountRepository contaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        if (clienteRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Usuário já existe.");
            return "cadastro";
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setUsername(username);
        cliente.setPasswordHash(passwordEncoder.encode(password));
        clienteRepository.save(cliente);

        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setCliente(cliente);
        contaRepository.save(account);

        return "redirect:/login?cadastroSucesso";
    }
}