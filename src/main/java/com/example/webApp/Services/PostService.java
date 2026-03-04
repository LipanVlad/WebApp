package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.PostDTO;
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
    public void checkAndSavePost(PostDTO postDTO, String communityName){
        if(postDTO.getTitle() == null || postDTO.getTitle().isEmpty()){
            throw new InvalidInputException("Post must have a valid title");
        }
        if(postDTO.getBody().isEmpty()){
            throw new InvalidInputException("Post must have a non-empty body");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();

        User owner = userRepo.findByUsername(loggedUserName)
                .orElseThrow(() -> new DoesNotExistException("User is not logged in anymore"));

        Community community = communityRepo.findByName(communityName)
                .orElseThrow(() -> new DoesNotExistException("Community does not exist anymore"));

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setBody(postDTO.getBody());
        post.setOwner(owner);
        post.setCommunity(community);
        postRepo.save(post);
    }
}
