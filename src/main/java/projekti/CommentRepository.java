package projekti;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPosted(LocalDateTime posted);

    List<Comment> findByPhoto(Photo photo);

    List<Comment> findByTweet(Tweet tweet);
}