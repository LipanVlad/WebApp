package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.CommentDTO;
import com.example.webApp.Entities.Comment;
import com.example.webApp.Entities.Post;
import com.example.webApp.Entities.User;
import com.example.webApp.Exceptions.DoesNotExistException;
import com.example.webApp.Exceptions.InvalidInputException;
import com.example.webApp.Repositories.CommentRepo;
import com.example.webApp.Repositories.PostRepo;
import com.example.webApp.Repositories.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    public CommentService(CommentRepo commentRepo, PostRepo postRepo, UserRepo userRepo){
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }
    public void checkAndSaveComment(CommentDTO commentDTO, Long postId){
        if(commentDTO.getBody() == null || commentDTO.getBody().isEmpty()){
            throw new InvalidInputException("Comment body must be valid");
        }
        Comment comment = new Comment();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();

        comment.setBody(commentDTO.getBody());
        Post post = postRepo.findById(postId)
                        .orElseThrow( () -> new DoesNotExistException("Post does not exist anymore"));
        comment.setPost(post);

        User owner = userRepo.findByUsername(loggedUserName)
                        .orElseThrow( () -> new DoesNotExistException("User does not exist anymore"));
        comment.setOwner(owner);
        comment.setCreationTime(LocalDateTime.now());
        commentRepo.save(comment);
    }
}
