package projekti;

import static org.junit.Assert.assertFalse;
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
public class TweetServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TweetRepository tweetRepository;

    @Test
    public void testCreateTweet() {
        Account account = new Account();

        account.setName("Maija Maitoparta");
        account.setUsername("maijis");
        account.setProfileString("maijis123");
        account.setPassword("kissanruoka");
        accountRepository.save(account);

        String content = "Hello Meow!";
        tweetService.create(content, account);

        assertFalse(tweetRepository.findAll().isEmpty());

        assertFalse(account.getTweets().isEmpty());

        String content2 = "Hello again!";
        tweetService.create(content2, account);

        // assertTrue(account.getTweets().size() == 2);
    }
}
