package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.PostDTO;
import com.example.webApp.Services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {
    private final PostService postService;
    public PostController(PostService postService){
        this.postService = postService;
    }
    @PostMapping("/{communityName}/add/post")
    public ResponseEntity<?> addPost(@RequestBody PostDTO postDTO, @PathVariable String communityName){
        postService.checkAndSavePost(postDTO, communityName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post added");
    }
}
