package projekti;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DefaultController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public String homePage() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountRepository.findByUsername(username);

        if (account != null) {
            return "redirect:/users/" + account.getProfileString();
        }
        return "index";
    }

}
