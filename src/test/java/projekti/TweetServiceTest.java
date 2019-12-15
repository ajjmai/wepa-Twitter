package projekti;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

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
    private AccountRepository accountRepository;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private CommentRepository commentRepository;

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

        assertTrue("tweetrepository contains one tweet", tweetRepository.findAll().size() != 0);

        String content2 = "Hello again!";
        tweetService.create(content2, account);

        assertTrue("tweetrepository contains two tweets", tweetRepository.findAll().size() == 2);

        assertTrue("account has two tweets", tweetRepository.findByOwner(account).size() == 2);
    }

    @Test
    public void testLikeTweet() {
        LocalDateTime posted = LocalDateTime.now();

        Account account = new Account();

        account.setName("Maija Maitoparta");
        account.setUsername("maijis");
        account.setProfileString("maijis123");
        account.setPassword("kissanruoka");
        accountRepository.save(account);

        Tweet tweet = new Tweet();
        tweet.setOwner(account);
        tweet.setPosted(posted);
        tweet.setContent("Hello Meow!");

        tweetRepository.save(tweet);

        Account account2 = new Account();

        account2.setName("Pekka Töpöhäntä");
        account2.setUsername("pekka_tp");
        account2.setProfileString("pekka_tp");
        account2.setPassword("kissanruoka");

        accountRepository.save(account2);

        tweetService.likeTweet(tweet, account2);

        assertTrue("tweet has one like", tweet.getLikesCount() == 1);
    }

    @Test
    public void testCommentTweet() {
        LocalDateTime posted = LocalDateTime.now();

        Account account = new Account();

        account.setName("Maija Maitoparta");
        account.setUsername("maijis");
        account.setProfileString("maijis123");
        account.setPassword("kissanruoka");
        accountRepository.save(account);

        Tweet tweet = new Tweet();
        tweet.setOwner(account);
        tweet.setPosted(posted);
        tweet.setContent("Hello Meow!");

        tweetRepository.save(tweet);

        Account account2 = new Account();

        account2.setName("Pekka Töpöhäntä");
        account2.setUsername("pekka_tp");
        account2.setProfileString("pekka_tp");
        account2.setPassword("kissanruoka");

        accountRepository.save(account2);

        tweetService.commentTweet("Hello there", tweet.getId(), account2);

        assertTrue("tweet has a comment", commentRepository.findByTweet(tweet).size() != 0);
    }
}
