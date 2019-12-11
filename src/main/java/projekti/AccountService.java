package projekti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Account> list() {
        return accountRepository.findAll();
    }

    public Account getOneUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account getOneProfileString(String profileString) {
        return accountRepository.findByProfileString(profileString);
    }

    public void create(Account account) {
        String encryptedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encryptedPassword);

        accountRepository.save(account);
    }
}
