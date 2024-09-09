package com.example.studently.repositories;

import com.example.studently.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    List<User> findByDescription(String description);
    User findByEmail(String email);
}
