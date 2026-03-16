package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.PostRequestDTO;
import com.example.webApp.DataTransferObjects.PostResponseDTO;
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

@Service
public class PostService {
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CommunityRepo communityRepo;
    public PostService(PostRepo postRepo, UserRepo userRepo, CommunityRepo communityRepo){
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.communityRepo = communityRepo;
    }

    private PostResponseDTO postToDTO(Post post){
        PostResponseDTO postResponseDTO = new PostResponseDTO();

        postResponseDTO.setId(post.getId());
        postResponseDTO.setBody(post.getBody());
        postResponseDTO.setCreationTime(post.getCreationTime());
        postResponseDTO.setOwnerId(post.getOwner().getId());
        postResponseDTO.setCommunityId(post.getCommunity().getId());
        postResponseDTO.setTitle(post.getTitle());


        return postResponseDTO;
    }
    public PostResponseDTO checkAndSavePost(PostRequestDTO postRequestDTO, String communityName){
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
}
