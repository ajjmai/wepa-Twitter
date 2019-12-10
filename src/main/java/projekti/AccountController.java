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
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    private AccountService accountService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

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

        if (accountService.getOneUsername(account.getUsername()) != null) {
            return "redirect:/index";
        }

        String encryptedPassword = passwordEncoder.encode(account.getPassword());

        account.setPassword(encryptedPassword);
        accountService.add(account);
        return "redirect:/index";
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", accountService.list());
        return "users";
    }

    @GetMapping("/users/{profileString}")
    public String getOne(Model model, @PathVariable String profileString) {
        Account account = accountService.getOneProfileString(profileString);
        model.addAttribute("user", account);

        model.addAttribute("tweets", tweetService.get25Tweets(account));

        String username = authenticationService.getUsername();
        if (username != null) {
            Account auth_account = accountService.getOneUsername(username);
            model.addAttribute("auth_user", auth_account);
        }

        return "user";
    }

    // @PreAuthorize("#username ==
    // SecurityContextHolder.getContext().getAuthentication().getUsername()")
    @PostMapping("/users/{profileString}")
    public String newTweet(@RequestParam @NotBlank @Size(max = 160) String content,
            @RequestParam @NotBlank String username) {

        Account account = accountService.getOneUsername(username);
        if (account == null) {
            return "redirect:/index";
        }

        LocalDateTime posted = LocalDateTime.now();

        Tweet tweet = new Tweet();
        tweet.setOwner(account);
        tweet.setPosted(posted);
        tweet.setContent(content);
        tweetService.add(tweet);
        return "redirect:/users/" + account.getProfileString();
    }

    // vain kirjautuneet
    @Transactional
    @PostMapping("/users/{profileString}/tweets/like")
    public String likeTweet(@PathVariable String profileString, @RequestParam Long id) {
        Tweet tweet = tweetService.getOneId(id);

        // user not authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account account = accountService.getOneUsername(username);
        if (account == null) {
            return "redirect:/users/" + profileString;
        }

        // // cannot like one's own tweet
        // if (account.getProfileString() == profileString) {
        // return "redirect:/users/" + profileString;
        // }

        // already liked this tweet
        if (voteService.getOneOwnerAndTweet(account, tweet) != null) {
            return "redirect:/users/" + profileString;
        }

        LocalDateTime liked = LocalDateTime.now();

        Vote like = new Vote();

        like.setOwner(account);
        like.setLiked(liked);
        like.setTweet(tweet);
        voteService.add(like);
        tweet.setLikesCount(tweet.getLikesCount() + 1);
        tweetService.add(tweet);

        return "redirect:/users/" + profileString;
    }

    @PostMapping("/users/{profileString}/tweets/comment")
    public String commentTweet(@PathVariable String profileString, @RequestParam Long id,
            @RequestParam String content) {
        Tweet tweet = tweetService.getOneId(id);
        LocalDateTime posted = LocalDateTime.now();

        // user not authenticated
        String username = authenticationService.getUsername();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account account = accountService.getOneUsername(username);
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
        commentService.add(comment);

        return "redirect:/users/" + profileString;
    }

}
