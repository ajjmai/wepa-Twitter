package projekti;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (username == null) {
            model.addAttribute("auth_user", null);
            return "index";
        }

        Account account = accountRepository.findByUsername(username);
        model.addAttribute("auth_user", account);

        return "index";
    }

}
