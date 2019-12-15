package projekti;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Follow extends AbstractPersistable<Long> {

    // The one who follows
    @ManyToOne
    private Account follower;

    // The one being followed
    @ManyToOne
    private Account target;

    private LocalDateTime startedFollowing;

    private Boolean blocked = false;
}