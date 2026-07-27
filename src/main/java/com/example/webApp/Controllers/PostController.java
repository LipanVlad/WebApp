package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.*;
import com.example.webApp.Services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<?> patchPost(@RequestBody PostRequestDTO postRequestDTO, @PathVariable Long postId){
        PostResponseDTO postResponseDTO = service.patchPost(postRequestDTO, postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(postResponseDTO);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId){
        PostResponseDTO postResponseDTO = service.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDTO);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(){
        List<PostResponseDTO> postResponseDTOList = service.getPosts();
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDTOList);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId) {
        List<CommentResponseDTO> comments = service.getCommentsByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
}
