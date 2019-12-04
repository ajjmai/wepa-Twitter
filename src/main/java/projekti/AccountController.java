package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/user")
    public String list(Model model) {
        model.addAttribute("users", accountRepository.findAll());
        return "user";
    }

    @GetMapping("/user/{id}")
    public String getOne(Model model, @PathVariable Long id) {
        model.addAttribute("user", accountRepository.getOne(id));
        return "user";
    }
}