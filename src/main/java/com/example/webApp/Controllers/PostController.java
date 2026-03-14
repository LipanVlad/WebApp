package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.PostRequestDTO;
import com.example.webApp.DataTransferObjects.PostResponseDTO;
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
    public ResponseEntity<?> addPost(@RequestBody PostRequestDTO postRequestDTO, @PathVariable String communityName){
        PostResponseDTO postResponseDTO = postService.checkAndSavePost(postRequestDTO, communityName);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }
}
