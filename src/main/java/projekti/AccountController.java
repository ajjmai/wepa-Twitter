package projekti;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Autowired
    // private UserDetailsService userDetailsService;

    @ModelAttribute
    private Account getAccount() {
        return new Account();
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String newAccount(@Valid @ModelAttribute("account") Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return "redirect:/index";
        }

        String encryptedPassword = passwordEncoder.encode(account.getPassword());

        account.setPassword(encryptedPassword);
        accountRepository.save(account);
        return "redirect:/index";
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", accountRepository.findAll());
        return "redirect:/index";
    }

    @GetMapping("/users/{profileString}")
    public String getOne(Model model, @PathVariable String profileString) {
        Account account = accountRepository.findByProfileString(profileString);
        model.addAttribute("user", account);
        model.addAttribute("tweets", tweetRepository.findByOwner(account));
        return "user";
    }

    // @PreAuthorize("#username ==
    // SecurityContextHolder.getContext().getAuthentication().getUsername()")
    @PostMapping("/users/{profileString}")
    public String newTweet(@RequestParam @NotBlank @Size(max = 160) String content,
            @RequestParam @NotBlank String username) {

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return "redirect:/index";
        }

        LocalDateTime posted = LocalDateTime.now();

        Tweet tweet = new Tweet(content, posted, account);
        tweetRepository.save(tweet);
        return "redirect:/users/" + account.getProfileString();
    }

}
