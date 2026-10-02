package vn.edu.fpt.auth.controller;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        model.addAttribute("accountName", session.getAttribute("loginAccountName"));
        session.removeAttribute("loginAccountName");
        return "auth/login";
    }

    @GetMapping("/admin/home")
    public String adminHome(Principal principal, Model model) {
        model.addAttribute("accountName", principal.getName());
        return "auth/admin-home";
    }

    @GetMapping("/customer/home")
    public String customerHome(Principal principal, Model model) {
        model.addAttribute("accountName", principal.getName());
        return "auth/customer-home";
    }

    @RequestMapping("/access-denied")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDenied() {
        return "auth/access-denied";
    }
}
