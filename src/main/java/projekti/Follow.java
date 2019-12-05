// package projekti;

// import java.time.LocalDateTime;

// import javax.persistence.Entity;
// import javax.persistence.ManyToMany;

// import org.springframework.data.jpa.domain.AbstractPersistable;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @NoArgsConstructor
// @AllArgsConstructor
// @Data
// public class Follow extends AbstractPersistable<Long> {

// // The one who follows
// @ManyToMany
// private Account follower;

// // The one being followed
// @ManyToMany
// private Account target;

// private LocalDateTime posted;

// private Boolean blocked;
// }