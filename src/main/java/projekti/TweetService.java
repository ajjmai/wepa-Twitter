package projekti;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Tweet> get25Tweets(Account account) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("posted").descending());

        // TO DO get also tweets by followers and extract 25 newest

        List<Tweet> tweets = tweetRepository.findByOwner(account, pageable);

        return tweets;
    }

    public List<Tweet> get15MostLiked() {
        Pageable pageable = PageRequest.of(0, 15, Sort.by("likesCount").descending());

        List<Tweet> tweets = tweetRepository.findAll(pageable).getContent();

        return tweets;
    }

    public List<Tweet> get15Newest() {
        Pageable pageable = PageRequest.of(0, 15, Sort.by("posted").descending());

        List<Tweet> tweets = tweetRepository.findAll(pageable).getContent();

        return tweets;
    }

    public void create(String content, Account account) {
        LocalDateTime posted = LocalDateTime.now();

        Tweet tweet = new Tweet();
        tweet.setOwner(account);
        tweet.setPosted(posted);
        tweet.setContent(content);

        tweetRepository.save(tweet);
    }

    public Tweet getOneId(Long id) {
        return tweetRepository.getOne(id);
    }

    @Transactional
    public void likeTweet(Tweet tweet, Account account) {
        LocalDateTime liked = LocalDateTime.now();

        Vote like = new Vote();
        like.setOwner(account);
        like.setLiked(liked);
        like.setTweet(tweet);
        voteRepository.save(like);
        tweet.setLikesCount(tweet.getLikesCount() + 1);
        tweetRepository.save(tweet);
    }

    public Vote getVoteOwnerAndTweet(Account account, Tweet tweet) {
        return voteRepository.findByOwnerAndTweet(account, tweet);
    }

    @Transactional
    public void commentTweet(String content, Long tweetId, Account account) {
        LocalDateTime posted = LocalDateTime.now();

        Tweet tweet = tweetRepository.getOne(tweetId);

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setOwner(account);
        comment.setTweet(tweet);
        comment.setPosted(posted);
        commentRepository.save(comment);
    }

}
