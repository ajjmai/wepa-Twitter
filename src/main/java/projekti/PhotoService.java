package projekti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AccountRepository accountRepository;


    public List<Photo> listByOwner(Account account) {
        return photoRepository.findByOwner(account);
    }

    public void add(Photo photo) {
        photoRepository.save(photo);
    }

    public Photo getOneId(Long id) {
        return photoRepository.getOne(id);
    }

    @Transactional
    public void addProfilePic(Account account, Long id) {
        Photo photo = this.getOneId(id);
        Photo currentProfilePic = account.getProfilePic();
        if (currentProfilePic != null) {
            currentProfilePic.setProfilePic(false);
        }
        account.setProfilePic(photo);
        photo.setProfilePic(true);
    }

}