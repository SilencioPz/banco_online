package source.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import source.repository.AccountRepository;
import source.model.Account;
import source.model.AccountRequest;

import java.math.BigDecimal;
import java.util.Optional;

@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/testdb",
        "spring.datasource.username=testuser",
        "spring.datasource.password=testpassword"
})

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccount() {
        AccountRequest request = new AccountRequest();
        request.setAccountHolderName("João");
        request.setBalance(new BigDecimal("1000.00"));

        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setBalance(new BigDecimal("1000.00"));

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        Account result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
    }

    @Test
    public void testDeposit() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.deposit(1L, new BigDecimal("500.00"));
        assertEquals(new BigDecimal("1500.00"), updatedAccount.getBalance());
    }

    @Test
    public void testWithdraw() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.withdraw(1L, new BigDecimal("300.00"));
        assertEquals(new BigDecimal("700.00"), updatedAccount.getBalance());
    }
}