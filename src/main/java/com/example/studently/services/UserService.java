package com.example.studently.services;

import com.example.studently.models.Image;
import com.example.studently.models.User;
import com.example.studently.models.enums.Role;
import com.example.studently.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listUsers(String description) {
        return userRepository.findByDescription(description);
    };

    public List<User> listUsers() {
        return userRepository.findAll();
    };

    public User getPostById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean saveUser(User user, MultipartFile file1, MultipartFile file2) throws IOException {
        if (userRepository.findByEmail(user.getEmail()) != null)
            return false;

        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Image image1, image2;

        if (file1.getSize() != 0) {
            image1 = toImage(file1);
            image1.setPreviewImage(true);
            user.addImage(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImage(file2);
            image2.setPreviewImage(false);
            user.addImage(image2);
        }

        User userFromDb = userRepository.save(user);
        userFromDb.setPreviewImageId(userFromDb.getImages().get(0).getId());
        userRepository.save(user);

        return true;
    }

    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null)
            return false;

        return passwordEncoder.matches(password, user.getPassword());
    }

    private Image toImage(MultipartFile file) throws IOException {
        Image image = new Image();

        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());

        return image;
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
