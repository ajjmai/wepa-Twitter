package projekti;

import java.io.IOException;

import javax.tools.FileObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/users/{profileString}/album")
    public String getPhotos(Model model, @PathVariable String profileString) {

        Account account = accountRepository.findByProfileString(profileString);

        // return 404
        if (account == null) {
            return "index";
        }

        model.addAttribute("user", account);
        model.addAttribute("photos", photoRepository.findByOwner(account));

        return "album";
    }

    // @PreAuthorize("#username ==
    // SecurityContextHolder.getContext().getAuthentication().getUsername()")
    @PostMapping("/users/{profileString}/album")
    public String savePhoto(@RequestParam("photo") MultipartFile photo, @RequestParam String description,
            @RequestParam String username, @PathVariable String profileString) throws IOException {
        Account account = accountRepository.findByUsername(username);

        if (account == null) {
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

    @GetMapping("/users/{profileString}/album/{id}")
    public String redirectToAlbum(@PathVariable String profileString) {
        return "redirect:/users/" + profileString + "/album";
    }
}
