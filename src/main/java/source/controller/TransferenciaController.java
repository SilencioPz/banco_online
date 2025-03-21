package source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.service.AccountService;
import source.service.ClienteService;
import source.model.Cliente;

import java.math.BigDecimal;

@Controller
public class TransferenciaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/transferencia")
    public String transferencia() {
        return "transferencia";
    }

    @PostMapping("/transferencia")
    public String processarTransferencia(
            @RequestParam String sourceUsername,
            @RequestParam String targetUsername,
            @RequestParam BigDecimal amount,
            Model model) {

        try {
            Cliente sourceCliente = clienteService.findByUsername(sourceUsername);
            if (sourceCliente == null) {
                throw new RuntimeException("Cliente de origem não encontrado");
            }

            Cliente targetCliente = clienteService.findByUsername(targetUsername);
            if (targetCliente == null) {
                throw new RuntimeException("Cliente de destino não encontrado");
            }

            accountService.transfer(sourceCliente.getId(), targetCliente.getId(), amount);
            BigDecimal saldoOrigem = accountService.findByClienteId(sourceCliente.getId())
                    .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"))
                    .getBalance();

            BigDecimal saldoDestino = accountService.findByClienteId(targetCliente.getId())
                    .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"))
                    .getBalance();

            model.addAttribute("transferenciaSucesso", true);
            model.addAttribute("saldoOrigem", saldoOrigem);
            model.addAttribute("saldoDestino", saldoDestino);

            return "transferencia";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "transferencia";
        }
    }
}