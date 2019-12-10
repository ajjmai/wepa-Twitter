package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public void add(Vote vote) {
        voteRepository.save(vote); 
    }

    public Vote getOneOwnerAndTweet(Account account, Tweet tweet) {
        return voteRepository.findByOwnerAndTweet(account, tweet);
    }

    public Vote getOneOwnerAndPhoto(Account account, Photo photo) {
        return voteRepository.findByOwnerAndPhoto(account, photo);
    }
}
