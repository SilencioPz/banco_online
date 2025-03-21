package source.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.Principal;
import java.util.Optional;

@Controller
public class DepositoController {

    @Autowired
    private AccountRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/deposito")
    public String deposito() {
        return "deposito";
    }

    @PostMapping("/deposito")
    @Transactional
    public String processarDeposito(
            @RequestParam BigDecimal valor,
            Principal principal,
            Model model) {

        String username = principal.getName();
        Cliente cliente = clienteRepository.findByUsername(username);

        if (cliente == null) {
            model.addAttribute("error", "Cliente não encontrado.");
            return "deposito";
        }

        Optional<Account> contaOptional = contaRepository.findByClienteId(cliente.getId());
        Account conta;

        if (contaOptional.isPresent()) {
            conta = contaOptional.get();
            BigDecimal novoSaldo = conta.getBalance().add(valor);
            conta.setBalance(novoSaldo);

        } else {
            conta = new Account();
            conta.setCliente(cliente);
            conta.setBalance(valor);
        }

        contaRepository.save(conta);
        return "redirect:/account?depositoSucesso";
    }
}