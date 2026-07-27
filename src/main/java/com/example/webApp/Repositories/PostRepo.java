package com.example.webApp.Repositories;

import com.example.webApp.Entities.Community;
import com.example.webApp.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepo extends JpaRepository<Post,Long> {
    Optional<Post> findById(Long id);
}
