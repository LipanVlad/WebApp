package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.PostRequestDTO;
import com.example.webApp.DataTransferObjects.PostResponseDTO;
import com.example.webApp.Services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {
    private final PostService service;
    public PostController(PostService postService){
        this.service = postService;
    }

    @PostMapping("/{communityName}/add/post")
    public ResponseEntity<?> addPost(@Valid @RequestBody PostRequestDTO postRequestDTO, @PathVariable String communityName){
        PostResponseDTO postResponseDTO = service.checkAndSavePost(postRequestDTO, communityName);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        service.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
