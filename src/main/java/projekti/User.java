package projekti;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends AbstractPersistable<Long> {
    private String userName;
    private String name;
    private String password;
    private String profileString;

    @OneToMany(mappedBy = "owner")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Tweet> tweets = new ArrayList<>();

    // @ManyToMany
    // private List<User> following = new ArrayList<>();
}