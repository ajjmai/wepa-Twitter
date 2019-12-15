package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFollowerAndTarget(Account follower, Account target);
}