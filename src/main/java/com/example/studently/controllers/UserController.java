package com.example.studently.controllers;

import com.example.studently.models.User;
import com.example.studently.repositories.UserRepository;
import com.example.studently.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, User user, Model model, HttpServletRequest request) throws IOException {
        if (!userService.saveUser(user, file1, file2)) {
            model.addAttribute("error", "User with this email already exists");
            return "registration";
        }
        request.getSession().setAttribute("user", user);
        return "redirect:/profile";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String authorizeUser(@RequestParam(name = "username") String email, @RequestParam(name = "password") String password, Model model, HttpServletRequest request) throws IOException {
        if (!userService.loginUser(email, password)) {
            model.addAttribute("error", "Wrong email or password!");
            return "login";
        }
        request.getSession().setAttribute("user", userRepository.findByEmail(email));
        model.addAttribute("user", userRepository.findByEmail(email));
        return "redirect:/profile";
    }

    @PostMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String openProfile(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("images", userService.getPostById(user.getId()).getImages());
        return "profile";
    }

    @GetMapping("/users")
    public String users(@RequestParam(name = "description", required = false) String description, Model model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping("/user/{id}")
    public String postInfo(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getPostById(id));
        model.addAttribute("image", userService.getPostById(id).getImages().get(0));
        return "user_info";
    }

    @ModelAttribute("isUser")
    public User getUser(HttpServletRequest request){
        return (User) request.getSession().getAttribute("user");
    }
}
