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

    @Autowired
    private FollowRepository followRepository;

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

    @Test
    public void testFollow() {
        Account account1 = new Account();

        account1.setName("Maija Maitoparta");
        account1.setUsername("maijis");
        account1.setProfileString("maijis123");
        account1.setPassword("kissanruoka");

        accountRepository.save(account1);

        Account account2 = new Account();

        account2.setName("Pekka Töpöhäntä");
        account2.setUsername("pekka_tp");
        account2.setProfileString("pekka_tp");
        account2.setPassword("kissanruoka");

        accountRepository.save(account2);

        accountService.follow(account1, account2);

        assertTrue(followRepository.findByFollowerAndTarget(account1, account2) != null);

    }
}
