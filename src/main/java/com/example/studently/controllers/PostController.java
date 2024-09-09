package com.example.studently.controllers;

import com.example.studently.models.Post;
import com.example.studently.models.User;
import com.example.studently.repositories.PostRepository;
import com.example.studently.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    public PostController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @GetMapping("/posts")
    public String posts(Model model, HttpServletRequest request) {
        model.addAttribute("posts", postService.listPosts());
        model.addAttribute("post", new Post());
        model.addAttribute("user", request.getSession().getAttribute("user"));
        return "posts";
    }

    @GetMapping("/post/{id}")
    public String postInfo(@PathVariable("id") long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "post_info";
    }

    @PostMapping("/post/create")
    public String createPost(Post post, HttpServletRequest request) {
        postService.savePost(post, (User) request.getSession().getAttribute("user"));
        return "redirect:/posts";
    }

    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @GetMapping("/post/edit/{id}")
    public String changePost(@PathVariable("id") long id, HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("post", postService.getPostById(id));
            return "post_edit";
        }
        return "redirect:/posts";
    }

    @PostMapping("/post/edit/{id}")
    public String postEdit(@PathVariable("id") long id, @RequestParam(name = "title") String title, @RequestParam(name = "description") String description, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            Post post = postRepository.findById(id).orElse(null);
            post.setTitle(title);
            post.setDescription(description);
            postRepository.save(post);
            return "redirect:/posts";
        }
        return "redirect:/profile";
    }

    @ModelAttribute("isUser")
    public User getUser(HttpServletRequest request){
        return (User) request.getSession().getAttribute("user");
    }
}
