package projekti;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        return "redirect:/index";
    }

    @GetMapping("/users/{profileString}")
    public String getOne(Model model, @PathVariable String profileString) {
        Account account = accountRepository.findByProfileString(profileString);
        model.addAttribute("user", account);

        Pageable pageable = PageRequest.of(0, 25, Sort.by("posted").descending());
        model.addAttribute("tweets", tweetRepository.findByOwner(account, pageable));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (username != null) {
            Account auth_account = accountRepository.findByUsername(username);
            model.addAttribute("auth_user", auth_account);
        }

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

        Tweet tweet = new Tweet();
        tweet.setOwner(account);
        tweet.setPosted(posted);
        tweet.setContent(content);
        tweetRepository.save(tweet);
        return "redirect:/users/" + account.getProfileString();
    }

    // vain kirjautuneet
    @PostMapping("/users/{profileString}/tweets/like")
    public String likeTweet(@PathVariable String profileString, @RequestParam Long id) {
        Tweet tweet = tweetRepository.getOne(id);

        // user not authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return "redirect:/users/" + profileString;
        }

        // // cannot like one's own tweet
        // if (account.getProfileString() == profileString) {
        // return "redirect:/users/" + profileString;
        // }

        // already liked this tweet
        if (voteRepository.findByOwnerAndTweet(account, tweet) != null) {
            return "redirect:/users/" + profileString;
        }

        LocalDateTime liked = LocalDateTime.now();

        Vote like = new Vote();

        like.setOwner(account);
        like.setLiked(liked);
        like.setTweet(tweet);
        voteRepository.save(like);
        tweet.setLikesCount(tweet.getLikesCount() + 1);
        tweetRepository.save(tweet);

        return "redirect:/users/" + profileString;
    }

    @PostMapping("/users/{profileString}/tweets/comment")
    public String commentTweet(@PathVariable String profileString, @RequestParam Long id,
            @RequestParam String content) {
        Tweet tweet = tweetRepository.getOne(id);
        LocalDateTime posted = LocalDateTime.now();

        // user not authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return "redirect:/users/" + profileString;
        }

        // // cannot comment one's own tweet
        // if (account.getProfileString() == profileString) {
        // return "redirect:/users/" + profileString;
        // }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setOwner(account);
        comment.setTweet(tweet);
        comment.setPosted(posted);
        commentRepository.save(comment);

        return "redirect:/users/" + profileString;
    }

}
