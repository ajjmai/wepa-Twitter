package projekti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);

    List<User> findByUserName(String userName);

    List<User> findByProfileString(String profileString);
}