package projekti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    public List<Tweet> get25Tweets(Account account) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("posted").descending());

        // TO DO get also tweets by followers and extract 25 newest

        List<Tweet> tweets = tweetRepository.findByOwner(account, pageable);

        return tweets;
    }

    public void add(Tweet tweet) {
        tweetRepository.save(tweet);
    }

    public Tweet getOneId(Long id) {
        return tweetRepository.getOne(id);
    }

}