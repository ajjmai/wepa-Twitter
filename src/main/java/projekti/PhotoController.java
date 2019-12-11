package projekti;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/users/{profileString}/album")
    public String getPhotos(Model model, @PathVariable String profileString) {

        Account account = accountService.getOneProfileString(profileString);

        // return 404
        if (account == null) {
            return "index";
        }

        model.addAttribute("user", account);
        model.addAttribute("photos", photoService.listByOwner(account));

        String username = authenticationService.getUsername();
        if (username != null) {
            Account auth_account = accountService.getOneUsername(username);
            model.addAttribute("auth_user", auth_account);
        }

        return "album";
    }

    // @PreAuthorize("#username ==
    // SecurityContextHolder.getContext().getAuthentication().getUsername()")
    @Transactional
    @PostMapping("/users/{profileString}/album")
    public String savePhoto(@RequestParam("photo") MultipartFile photo, @RequestParam String description,
            @RequestParam String username, @PathVariable String profileString) throws IOException {
        Account account = accountService.getOneUsername(username);

        if (account == null) {
            return "redirect:/users/" + profileString + "/album";
        }
        if (photoService.listByOwner(account).size() >= 10) {
            return "redirect:/users/" + profileString + "/album";
        }

        Photo newPhoto = new Photo();
        newPhoto.setContent(photo.getBytes());
        newPhoto.setDescription(description);
        newPhoto.setOwner(account);
        photoRepository.save(newPhoto);
        return "redirect:/users/" + account.getProfileString() + "/album";
    }

    @GetMapping(path = "/users/{profileString}/album/{id}", produces = "image/png")
    @ResponseBody
    public byte[] getPhoto(@PathVariable Long id, @PathVariable String profileString) {
        return photoRepository.getOne(id).getContent();
    }

    // @GetMapping("/users/{profileString}/album/{id}")
    // public String redirectToAlbum(@PathVariable String profileString) {
    // return "redirect:/users/" + profileString + "/album";
    // }

    // vain kirjautuneet
    @Transactional
    @PostMapping("/users/{profileString}/album/like")
    public String likePhoto(@PathVariable String profileString, @RequestParam Long id) {
        Photo photo = photoService.getOneId(id);

        // user not authenticated
        String username = authenticationService.getUsername();
        if (username == null) {
            return "redirect:/users/" + profileString + "/album";
        }

        // no user account found
        Account account = accountService.getOneUsername(username);
        if (account == null) {
            return "redirect:/users/" + profileString + "/album";
        }

        // // cannot like one's own tweet
        // if (account.getProfileString() == profileString) {
        // return "redirect:/users/" + profileString + "/album";
        // }

        // already liked this tweet
        if (voteService.getOneOwnerAndPhoto(account, photo) != null) {
            return "redirect:/users/" + profileString + "/album";
        }

        LocalDateTime liked = LocalDateTime.now();

        Vote like = new Vote();

        like.setOwner(account);
        like.setLiked(liked);
        like.setPhoto(photo);
        voteService.add(like);
        photo.setLikesCount(photo.getLikesCount() + 1);
        photoRepository.save(photo);

        return "redirect:/users/" + profileString + "/album";
    }

    // vain seuraajat
    @Transactional
    @PostMapping("/users/{profileString}/album/comment")
    public String commentPhoto(@PathVariable String profileString, @RequestParam Long id,
            @RequestParam String content) {
        Photo photo = photoService.getOneId(id);
        LocalDateTime posted = LocalDateTime.now();

        // user not authenticated
        String username = authenticationService.getUsername();
        if (username == null) {
            return "redirect:/users/" + profileString + "/album";
        }

        // no user account found
        Account account = accountService.getOneUsername(username);
        if (account == null) {
            return "redirect:/users/" + profileString + "/album";
        }

        // // cannot comment one's own tweet
        // if (account.getProfileString() == profileString) {
        // return "redirect:/users/" + profileString;
        // }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setOwner(account);
        comment.setPhoto(photo);
        comment.setPosted(posted);
        commentService.add(comment);

        return "redirect:/users/" + profileString + "/album";
    }

    // TODO: vain kirjautunut käyttäjä omalla sivulla
    @Transactional
    @PostMapping("/users/{profileString}/album/profilepic")
    public String makeProfilePic(@PathVariable String profileString, @RequestParam Long id) {
        String username = this.authenticationService.getUsername();

        Account account = accountService.getOneProfileString(profileString);

        if (username != account.getUsername()) {
            return "redirect:/users/" + profileString + "/album";

        }

        this.photoService.addProfilePic(account, id);
        return "redirect:/users/" + profileString + "/album";
    }

    @Transactional
    @DeleteMapping("/users/{profileString}/album/delete")
    public String deletePhoto(@PathVariable String profileString, @RequestParam Long id) {
        String username = this.authenticationService.getUsername();

        Account account = accountService.getOneProfileString(profileString);

        if (username != account.getUsername()) {
            return "redirect:/users/" + profileString + "/album";
        }

        photoService.remove(account, id);

        return "redirect:/users/" + profileString + "/album";
    }

}
