package projekti;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.jpa.domain.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vote extends AbstractPersistable<Long> {

    @ManyToOne
    private Photo photo;

    @ManyToOne
    private Tweet tweet;

    private LocalDateTime liked;

    // the user who liked
    @ManyToOne
    private Account owner;
}