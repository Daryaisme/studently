package com.example.studently.services;

import com.example.studently.models.Post;
import com.example.studently.models.User;
import com.example.studently.repositories.PostRepository;
import com.example.studently.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> listPosts(String title) {
        return postRepository.findByTitle(title);
    };

    public List<Post> listPosts() {
        return postRepository.findAll();
    };

    public Post getPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void savePost(Post post, User user) {
        post.setUser(user);
        postRepository.save(post);
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }
}
