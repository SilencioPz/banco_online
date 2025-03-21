package source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.repository.AccountRepository;
import source.model.Account;
import source.model.AccountRequest;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(AccountRequest request) {
        Account account = new Account();
        account.setBalance(request.getBalance());
        return accountRepository.save(account);
    }

    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }

    public Account withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }

    public void transfer(Long sourceClienteId, Long targetClienteId, BigDecimal amount) {
        Account sourceAccount = accountRepository.findByClienteId(sourceClienteId)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

        Account targetAccount = accountRepository.findByClienteId(targetClienteId)
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Saldo insuficiente para transferência");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }

    public Optional<Account> findByClienteId(Long clienteId) {
        return accountRepository.findByClienteId(clienteId);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }
}