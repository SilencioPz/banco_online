package source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.repository.AccountRepository;
import source.repository.ClienteRepository;
import source.model.Account;
import source.model.Cliente;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/account")
    public String conta(Model model, Principal principal) {
        String username = principal.getName();

        Cliente cliente = clienteRepository.findByUsername(username);

        if (cliente != null) {
            Optional<Account> contaOptional = contaRepository.findByClienteId(cliente.getId());

            if (contaOptional.isPresent()) {
                BigDecimal saldo = contaOptional.get().getBalance();
                model.addAttribute("saldo", saldo);
            } else {
                model.addAttribute("saldo", BigDecimal.ZERO);
            }
        } else {
            model.addAttribute("saldo", BigDecimal.ZERO);
        }

        return "account";
    }
}