package projekti;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FollowRepository followRepository;

    public List<Account> list() {
        return accountRepository.findAll();
    }

    public Account getOneUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account getOneProfileString(String profileString) {
        return accountRepository.findByProfileString(profileString);
    }

    public void create(Account account) {
        String encryptedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encryptedPassword);

        accountRepository.save(account);
    }

    public void follow(Account follower, Account target) {
        LocalDateTime started = LocalDateTime.now();

        if (followRepository.findByFollowerAndTarget(follower, target) == null) {
            Follow relationship = new Follow();
            relationship.setFollower(follower);
            relationship.setTarget(target);
            relationship.setStartedFollowing(started);
            followRepository.save(relationship);
        }
    }

    // @Transactional
    // public void block(Account follower, Account target) {
    // Follow relationship = followRepository.findByFollowerAndTarget(follower,
    // target);

    // Follow otherWayRelationship =
    // followRepository.findByFollowerAndTarget(target, follower);

    // relationship.setBlocked(true);
    // otherWayRelationship.setBlocked(true);
    // }
}
