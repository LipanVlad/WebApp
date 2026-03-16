package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.CommentRequestDTO;
import com.example.webApp.DataTransferObjects.CommentResponseDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    public CommentService(CommentRepo commentRepo, PostRepo postRepo, UserRepo userRepo){
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    private CommentResponseDTO commentToDTO(Comment comment){
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();

        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setBody(comment.getBody());
        commentResponseDTO.setCreationTime(comment.getCreationTime());
        commentResponseDTO.setUserId(comment.getOwner().getId());
        commentResponseDTO.setPostId(comment.getPost().getId());
        if (comment.getParentComment() != null)
            commentResponseDTO.setParentCommentId(comment.getParentComment().getId());
        else{
            commentResponseDTO.setParentCommentId(null);
        }
        List<CommentResponseDTO> repliesToDTO = comment.getReplies().stream()
                .map(this::commentToDTO)
                .toList();

        commentResponseDTO.setReplies(repliesToDTO);

        return commentResponseDTO;
    }

    public CommentResponseDTO checkAndSaveRootComment(CommentRequestDTO commentRequestDTO, Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();

        Comment comment = new Comment();
        comment.setBody(commentRequestDTO.getBody());
        Post post = postRepo.findById(postId)
                        .orElseThrow( () -> new DoesNotExistException("Post does not exist anymore"));
        comment.setPost(post);

        User owner = userRepo.findByUsername(loggedUserName)
                        .orElseThrow( () -> new DoesNotExistException("User does not exist anymore"));
        comment.setOwner(owner);
        comment.setCreationTime(LocalDateTime.now());
        comment.setParentComment(null);


        commentRepo.save(comment);
        CommentResponseDTO commentResponseDTO = commentToDTO(comment);
        return commentResponseDTO;
    }

    public CommentResponseDTO checkAndSaveChildComment(CommentRequestDTO commentRequestDTO, Long parentCommentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();

        Comment comment = new Comment();
        comment.setBody(commentRequestDTO.getBody());
        comment.setCreationTime(LocalDateTime.now());
        User owner = userRepo.findByUsername(loggedUserName)
                .orElseThrow( () -> new DoesNotExistException("User does not exist anymore"));
        comment.setOwner(owner);

        Comment parentComment = commentRepo.findById(parentCommentId)
                .orElseThrow( () -> new DoesNotExistException("Parent comment does not exist anymore"));

        comment.setParentComment(parentComment);
        comment.setPost(parentComment.getPost());


        commentRepo.save(comment);
        CommentResponseDTO commentResponseDTO = commentToDTO(comment);
        return commentResponseDTO;
    }
}
