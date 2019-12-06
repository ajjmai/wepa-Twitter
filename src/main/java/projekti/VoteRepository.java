package projekti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByPhoto(Photo photo);

    List<Vote> findByTweet(Tweet tweet);

    Vote findByOwnerAndPhoto(Account account, Photo photo);

    Vote findByOwnerAndTweet(Account account, Tweet tweet);

}