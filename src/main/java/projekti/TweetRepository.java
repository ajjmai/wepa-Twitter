package projekti;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByPosted(LocalDateTime posted);

    List<Tweet> findByOwner(Account owner);
}