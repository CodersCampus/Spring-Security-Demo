package com.coderscampus.security.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginLogoutController {

    @GetMapping("/login")
    public String loginGet () {
        return "login";
    }
    
    @GetMapping("/login-error")
    public String loginError (Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
