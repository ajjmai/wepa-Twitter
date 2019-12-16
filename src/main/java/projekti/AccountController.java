package projekti;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private FollowRepository followRepository;

    @ModelAttribute
    private Account getAccount() {
        return new Account();
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // create new account
    @PostMapping("/register")
    public String newAccount(@Valid @ModelAttribute("account") Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        if (accountService.getOneUsername(account.getUsername()) != null) {
            return "redirect:/home";
        }

        accountService.create(account);
        return "redirect:/home";
    }

    // get all users
    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", accountService.list());

        String username = authenticationService.getUsername();
        if (username != null) {
            Account auth_account = accountService.getOneUsername(username);
            model.addAttribute("auth_user", auth_account);
        }

        return "users";
    }

    // get user profilepage
    @GetMapping("/users/{profileString}")
    public String getOne(Model model, @PathVariable String profileString) {

        Account account = accountService.getOneProfileString(profileString);
        if (account == null) {
            return "redirect:/home";
        }

        model.addAttribute("user", account);
        model.addAttribute("tweets", tweetService.get25Tweets(account));

        String username = authenticationService.getUsername();
        if (username != null) {
            Account auth_account = accountService.getOneUsername(username);
            model.addAttribute("auth_user", auth_account);
        }

        return "user";
    }

    // get profile picture
    @GetMapping(path = "/users/{profileString}/profilepic", produces = "image/png")
    @ResponseBody
    public byte[] getPhoto(@PathVariable String profileString) {
        Account account = accountService.getOneProfileString(profileString);
        return account.getProfilePic().getContent();
    }

    // create new tweet
    @PostMapping("/users/{profileString}")
    public String newTweet(@RequestParam @NotBlank @Size(max = 160) String content,
            @PathVariable String profileString) {

        // user not authenticated
        String username = authenticationService.getUsername();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account auth_account = accountService.getOneUsername(username);
        if (auth_account == null) {
            return "redirect:/users/" + profileString;
        }

        // cannot tweet on someone else's page
        if (auth_account.getProfileString().equals(profileString)) {
            tweetService.create(content, auth_account);
            return "redirect:/users/" + profileString;
        }

        return "redirect:/users/" + profileString;
    }

    // like tweet
    @Transactional
    @PostMapping("/users/{profileString}/tweets/like")
    public String likeTweet(@PathVariable String profileString, @RequestParam Long id) {
        Tweet tweet = tweetService.getOneId(id);

        // user not authenticated
        String username = authenticationService.getUsername();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account auth_account = accountService.getOneUsername(username);
        if (auth_account == null) {
            return "redirect:/users/" + profileString;
        }

        // cannot like one's own tweet
        if (auth_account.getProfileString().equals(profileString)) {
            return "redirect:/users/" + profileString;
        }

        // already liked this tweet
        if (tweetService.getVoteOwnerAndTweet(auth_account, tweet) != null) {
            return "redirect:/users/" + profileString;
        }

        tweetService.likeTweet(tweet, auth_account);

        return "redirect:/users/" + profileString;
    }

    // comment tweet
    @PostMapping("/users/{profileString}/tweets/comment")
    public String commentTweet(@PathVariable String profileString, @RequestParam Long id,
            @RequestParam String content) {

        // user not authenticated
        String username = authenticationService.getUsername();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account auth_account = accountService.getOneUsername(username);
        if (auth_account == null) {
            return "redirect:/users/" + profileString;
        }

        Account target = accountService.getOneProfileString(profileString);
        if (target == null) {
            return "redirect:/users/" + profileString;
        }

        if (followRepository.findByFollowerAndTarget(auth_account, target) == null) {
            return "redirect:/users/" + profileString;
        }

        tweetService.commentTweet(content, id, auth_account);

        return "redirect:/users/" + profileString;
    }

    // follow
    @PostMapping("/users/{profileString}/follow")
    public String follow(@PathVariable String profileString) {
        // user not authenticated
        String username = authenticationService.getUsername();
        if (username == null) {
            return "redirect:/users/" + profileString;
        }

        // no user account found
        Account auth_account = accountService.getOneUsername(username);
        if (auth_account == null) {
            return "redirect:/users/" + profileString;
        }

        // cannot follow oneself
        Account targetAccount = accountService.getOneProfileString(profileString);
        if (auth_account.getUsername().equals(targetAccount.getUsername())) {
            return "redirect:/users/" + profileString;
        }

        accountService.follow(auth_account, targetAccount);

        return "redirect:/users/" + profileString;
    }

}
