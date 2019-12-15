package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);

    Account findByUsername(String username);

    Account findByProfileString(String profileString);
}