package projekti;

import org.apache.xpath.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByOwner(Account owner);
}