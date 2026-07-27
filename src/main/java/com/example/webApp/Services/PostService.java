package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.*;
import com.example.webApp.Entities.Comment;
import com.example.webApp.Entities.Community;
import com.example.webApp.Entities.Post;
import com.example.webApp.Entities.User;
import com.example.webApp.Exceptions.DoesNotExistException;
import com.example.webApp.Exceptions.InvalidInputException;
import com.example.webApp.Repositories.CommunityRepo;
import com.example.webApp.Repositories.PostRepo;
import com.example.webApp.Repositories.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CommunityRepo communityRepo;
    private final CommentService commentService;

    public PostService(PostRepo postRepo, UserRepo userRepo, CommunityRepo communityRepo, CommentService commentService) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.communityRepo = communityRepo;
        this.commentService = commentService;
    }

    public PostResponseDTO postToDTO(Post post) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();

        postResponseDTO.setId(post.getId());
        postResponseDTO.setBody(post.getBody());
        postResponseDTO.setCreationTime(post.getCreationTime());
        postResponseDTO.setOwnerId(post.getOwner().getId());
        postResponseDTO.setCommunityId(post.getCommunity().getId());
        postResponseDTO.setTitle(post.getTitle());


        return postResponseDTO;
    }

    public PostResponseDTO checkAndSavePost(PostRequestDTO postRequestDTO, String communityName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();

        User owner = userRepo.findByUsername(loggedUserName)
                .orElseThrow(() -> new DoesNotExistException("User is not logged in anymore"));

        Community community = communityRepo.findByName(communityName)
                .orElseThrow(() -> new DoesNotExistException("Community does not exist anymore"));

        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setBody(postRequestDTO.getBody());
        post.setOwner(owner);
        post.setCommunity(community);
        post.setCreationTime(LocalDateTime.now());
        postRepo.save(post);
        PostResponseDTO postResponseDTO = postToDTO(post);
        return postResponseDTO;
    }

    public void deletePost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new DoesNotExistException("Post not found"));
        postRepo.delete(post);
    }

    public PostResponseDTO patchPost(PostRequestDTO postRequestDTO, Long postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new DoesNotExistException("Post does not exist"));
        if(postRequestDTO.getTitle()!=null && !postRequestDTO.getTitle().equalsIgnoreCase(post.getTitle())){
            post.setTitle(postRequestDTO.getTitle());
        }
        if(postRequestDTO.getBody()!=null && !postRequestDTO.getBody().equalsIgnoreCase(post.getBody())){
            post.setBody(postRequestDTO.getBody());
        }

        postRepo.save(post);
        return postToDTO(post);
    }

    public PostResponseDTO getPost(Long postId){
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new DoesNotExistException("Post does not exist"));
        PostResponseDTO postResponseDTO = postToDTO(post);
        return postResponseDTO;
    }
    public List<PostResponseDTO> getPosts(){
        List<Post> posts = postRepo.findAll();
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        for (Post p : posts) {
            postResponseDTOList.add(postToDTO(p));
        }
        return postResponseDTOList;
    }

    public List<CommentResponseDTO> getCommentsByPostId(Long postId){
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new DoesNotExistException("Post does not exist"));
        List<Comment> commentList = post.getCommentList();
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();

        for (Comment c : commentList) {
            if (c.getParentComment() == null) {
                commentResponseDTOList.add(commentService.commentToDTO(c));
            }
        }
        return commentResponseDTOList;
    }
}

