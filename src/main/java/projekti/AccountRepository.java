package projekti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByName(String name);

    List<Account> findByUsername(String userName);

    List<Account> findByProfileString(String profileString);
}