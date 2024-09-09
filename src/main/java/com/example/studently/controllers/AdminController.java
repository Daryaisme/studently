package com.example.studently.controllers;

import com.example.studently.models.Post;
import com.example.studently.models.User;
import com.example.studently.models.enums.Role;
import com.example.studently.repositories.PostRepository;
import com.example.studently.repositories.UserRepository;
import com.example.studently.services.PostService;
import com.example.studently.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public AdminController(UserService userService, PostService postService, UserRepository userRepository, PostRepository postRepository) {
        this.userService = userService;
        this.postService = postService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/admin")
    public String isAdmin(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("images", userService.getPostById(user.getId()).getImages());
        if (user != null && user.isAdmin()) {
            return "admin";
        }
        return "redirect:/profile";
    }

    @GetMapping("/admin/users")
    public String adminUsers(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            model.addAttribute("users", userService.listUsers());
            return "admin_users";
        }
        return "redirect:/profile";
    }

    @GetMapping("/admin/posts")
    public String adminPosts(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            model.addAttribute("posts", postService.listPosts());
            return "admin_posts";
        }
        return "redirect:/profile";
    }

    @PostMapping("/admin/user/delete/{id}")
    public String userBan(@PathVariable("id") long id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            user = userRepository.findById(id).orElse(null);
            if (user != null) {
                userService.deleteUser(id);
            }
            return "redirect:/admin/users";
        }
        return "redirect:/profile";
    }

    @PostMapping("/admin/post/delete/{id}")
    public String postBan(@PathVariable("id") long id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            Post post = postRepository.findById(id).orElse(null);
            if (user != null) {
                postService.deletePost(id);
            }
            return "redirect:/admin/posts";
        }
        return "redirect:/profile";
    }

    @PostMapping("/admin/user/edit/{id}")
    public String userEdit(@PathVariable("id") long id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            user = userRepository.findById(id).orElse(null);
            if (user != null) {
                user.getRoles().add(Role.ROLE_ADMIN);
                userRepository.save(user);
            }
            return "redirect:/admin/users";
        }
        return "redirect:/profile";
    }

    @GetMapping("/admin/post/edit/{id}")
    public String changePost(@PathVariable("id") long id, HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            model.addAttribute("post", postService.getPostById(id));
            return "post_edit";
        }
        return "redirect:/profile";
    }

    @PostMapping("/admin/post/edit/{id}")
    public String postEdit(@PathVariable("id") long id, @RequestParam(name = "title") String title, @RequestParam(name = "description") String description, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.isAdmin()) {
            Post post = postRepository.findById(id).orElse(null);
            if (user != null) {
                post.setTitle(title);
                post.setDescription(description);
                postRepository.save(post);
            }
            return "redirect:/admin/posts";
        }
        return "redirect:/profile";
    }

    @ModelAttribute("isUser")
    public User getUser(HttpServletRequest request){
        return (User) request.getSession().getAttribute("user");
    }
}
