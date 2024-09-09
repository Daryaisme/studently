package com.example.studently.controllers;

import com.example.studently.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {
    @GetMapping("/")
    public String start() {
        return "start";
    }

    @ModelAttribute("isUser")
    public User getUser(HttpServletRequest request){
        return (User) request.getSession().getAttribute("user");
    }
}
