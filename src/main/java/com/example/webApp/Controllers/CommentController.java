package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.CommentDTO;
import com.example.webApp.Services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service){
        this.service = service;
    }
    @PostMapping("{postId}/add/comment")
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO, @PathVariable Long postId){
        service.checkAndSaveComment(commentDTO, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment added");
    }
}
