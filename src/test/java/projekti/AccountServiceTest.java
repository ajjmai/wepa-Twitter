package projekti;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testCreateAccount() {
        Account account = new Account();

        account.setName("Maija Maitoparta");
        account.setUsername("maijis");
        account.setProfileString("maijis123");
        account.setPassword("kissanruoka");

        accountService.create(account);

        assertTrue(accountService.getOneUsername("maijis") != null);
        assertTrue(accountService.getOneUsername("maijis").getName() == "Maija Maitoparta");
    }
}
