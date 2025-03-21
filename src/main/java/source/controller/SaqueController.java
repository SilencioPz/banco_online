package source.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.service.AccountService;
import source.service.ClienteService;
import source.model.Account;
import source.model.Cliente;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@Controller
public class SaqueController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/saque")
    public String saque() {
        return "saque";
    }

    @PostMapping("/saque")
    @Transactional
    public String processarSaque(
            @RequestParam BigDecimal amount,
            Principal principal,
            Model model) {

        String username = principal.getName();
        Cliente cliente = clienteService.findByUsername(username);

        if (cliente == null) {
            model.addAttribute("error", "Cliente não encontrado.");
            return "saque";
        }

        Optional<Account> contaOptional = accountService.findByClienteId(cliente.getId());

        if (contaOptional.isPresent()) {
            Account conta = contaOptional.get();
            BigDecimal saldoAtual = conta.getBalance();

            if (saldoAtual.compareTo(amount) >= 0) {
                BigDecimal novoSaldo = saldoAtual.subtract(amount);
                conta.setBalance(novoSaldo);
                accountService.save(conta);

                model.addAttribute("saqueSucesso", true);
                model.addAttribute("saldoAtual", novoSaldo);

                return "saque";
            } else {
                model.addAttribute("error", "Saldo insuficiente para realizar o saque.");
                return "saque";
            }
        } else {
            model.addAttribute("error", "Conta não encontrada.");
            return "saque";
        }
    }
}