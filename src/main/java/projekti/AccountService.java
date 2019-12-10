package projekti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;
    
    public List<Account> list() {
        return accountRepository.findAll();
    }

    public Account getOneUsername(String username) {
        return accountRepository.findByUsername(username); 
    }

    public Account getOneProfileString(String profileString) {
        return accountRepository.findByProfileString(profileString);
    }

    public void add(Account account) {
        accountRepository.save(account);
    }
} 
