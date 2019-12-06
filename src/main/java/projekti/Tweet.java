package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tweet extends AbstractPersistable<Long> {
    @Size(max = 160)
    private String content;
    private LocalDateTime posted;

    @ManyToOne
    private Account owner;

    @OneToMany(mappedBy = "tweet")
    private List<Comment> comments = new ArrayList<>();

    private Integer likesCount = 0;

    @OneToMany(mappedBy = "tweet")
    private List<Vote> likes = new ArrayList<>();
}