package projekti;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment extends AbstractPersistable<Long> {

    @NotEmpty
    @Size(max = 200)
    private String content;

    private LocalDateTime posted;

    @ManyToOne
    private Photo photo;

    @ManyToOne
    private Tweet tweet;

    @ManyToOne
    private Account owner;

}