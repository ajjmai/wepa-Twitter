package projekti;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
public class Account extends AbstractPersistable<Long> {

    @NotEmpty
    @Size(min = 4, max = 30)
    @Column(unique = true)
    private String username;

    @NotEmpty
    @Size(max = 30)
    private String name;

    @NotEmpty
    private String password;

    @NotEmpty
    @Size(min = 4, max = 30)
    @Column(unique = true)
    private String profileString;

    @OneToMany(mappedBy = "owner")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Tweet> tweets = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Vote> likes = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Comment> comments = new ArrayList<>();

    // @ManyToMany
    // private List<User> following = new ArrayList<>();
}