package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.CommentRequestDTO;
import com.example.webApp.DataTransferObjects.CommentResponseDTO;
import com.example.webApp.DataTransferObjects.CommunityPatchDTO;
import com.example.webApp.DataTransferObjects.CommunityResponseDTO;
import com.example.webApp.Services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service){
        this.service = service;
    }
    @PostMapping("/{postId}/add/root/comment")
    public ResponseEntity<?> addRootComment(@Valid @RequestBody CommentRequestDTO commentRequestDTO, @PathVariable Long postId){
        CommentResponseDTO commentResponseDTO = service.checkAndSaveRootComment(commentRequestDTO, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }

    @PostMapping("/{parentCommentId}/add/child/comment")
    public ResponseEntity<?> addChildComment (@Valid @RequestBody CommentRequestDTO commentRequestDTO, @PathVariable Long parentCommentId){
        CommentResponseDTO commentResponseDTO = service.checkAndSaveChildComment(commentRequestDTO, parentCommentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        service.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<?> patchComment(@RequestBody CommentRequestDTO commentRequestDTO,@PathVariable Long commentId) {
        CommentResponseDTO commentResponseDTO = service.patchComment(commentRequestDTO,commentId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commentResponseDTO);
    }
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long commentId) {
        CommentResponseDTO commentResponseDTO = service.getComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDTO);
    }
    @GetMapping("/comments")
    public ResponseEntity<?> getComments() {
        List<CommentResponseDTO> commentResponseDTOList = service.getComments();
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDTOList);
    }
}
