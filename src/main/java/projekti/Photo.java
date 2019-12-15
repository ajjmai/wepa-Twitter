package projekti;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Photo extends AbstractPersistable<Long> {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;

    private String description;

    @ManyToOne
    private Account owner;

    @OneToMany(mappedBy = "photo")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "photo")
    private List<Vote> likes = new ArrayList<>();

    private Integer likesCount = 0;

    private Boolean profilePic = false;
}