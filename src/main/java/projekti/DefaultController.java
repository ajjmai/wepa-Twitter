package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TweetService tweetService;

    @GetMapping("/")
    public String homePage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        String username = authenticationService.getUsername();
        if (username == null) {
            model.addAttribute("auth_user", null);
            return "home";
        }

        Account account = accountService.getOneUsername(username);
        model.addAttribute("auth_user", account);

        model.addAttribute("mostLiked", tweetService.get15MostLiked());

        model.addAttribute("mostRecent", tweetService.get15Newest());

        return "home";
    }

    // @GetMapping("/login")
    // public String login() {
    // return "login";
    // }

}
