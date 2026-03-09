package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.CommentRequestDTO;
import com.example.webApp.DataTransferObjects.CommentResponseDTO;
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
    @PostMapping("{postId}/add/root/comment")
    public ResponseEntity<?> addRootComment(@RequestBody CommentRequestDTO commentRequestDTO, @PathVariable Long postId){
        CommentResponseDTO commentResponseDTO = service.checkAndSaveRootComment(commentRequestDTO, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }

    @PostMapping("{parentCommentId}/add/child/comment")
    public ResponseEntity<?> addChildComment (@RequestBody CommentRequestDTO commentRequestDTO, @PathVariable Long parentCommentId){
        CommentResponseDTO commentResponseDTO = service.checkAndSaveChildComment(commentRequestDTO, parentCommentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }
}
