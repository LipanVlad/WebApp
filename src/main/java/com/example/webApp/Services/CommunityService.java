package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.*;
import com.example.webApp.Entities.Community;
import com.example.webApp.Entities.Post;
import com.example.webApp.Entities.User;
import com.example.webApp.Exceptions.DoesNotExistException;
import com.example.webApp.Exceptions.InvalidInputException;
import com.example.webApp.Exceptions.NameAlreadyExistsException;
import com.example.webApp.Repositories.CommunityRepo;
import com.example.webApp.Repositories.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CommunityService {
    private final CommunityRepo communityRepo;
    private final UserRepo userRepo;
    private final PostService postService;
    public CommunityService(CommunityRepo communityRepo, UserRepo userRepo, PostService postService){
        this.communityRepo = communityRepo;
        this.userRepo = userRepo;
        this.postService = postService;

    }

    private CommunityResponseDTO communityToDTO (Community community){
        CommunityResponseDTO communityResponseDTO = new CommunityResponseDTO();

        communityResponseDTO.setId(community.getId());
        communityResponseDTO.setName(community.getName());
        communityResponseDTO.setDescription(community.getDescription());
        communityResponseDTO.setCreationTime(community.getCreationTime());
        communityResponseDTO.setOwnerId(community.getOwner().getId());
        return communityResponseDTO;
    }
    public CommunityResponseDTO checkAndSaveCommunity(CommunityRequestDTO communityRequestDTO){
        if(communityRepo.findByName(communityRequestDTO.getName()).isPresent()){
            throw new NameAlreadyExistsException("Community name is taken");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();
        Community community = new Community();
        community.setName(communityRequestDTO.getName());
        community.setDescription(communityRequestDTO.getDescription());
        User owner = userRepo.findByUsername(loggedUserName)
                .orElseThrow(() -> new DoesNotExistException("User is not logged in anymore"));
        community.setOwner(owner);
        community.setCreationTime(LocalDateTime.now());
        communityRepo.save(community);

        CommunityResponseDTO communityResponseDTO = communityToDTO(community);
        return communityResponseDTO;
    }

    public void deleteCommunity(Long communityId){
        Community community = communityRepo.findById(communityId)
                        .orElseThrow( () -> new DoesNotExistException("Community not found"));

        communityRepo.delete(community);

    }
    public CommunityResponseDTO patchCommunity(CommunityPatchDTO communityPatchDTO, String currentName){
        Community community = communityRepo.findByName(currentName)
                .orElseThrow(() -> new DoesNotExistException("Community does not exist "));

        if(communityPatchDTO.getName()!=null && !community.getName().equalsIgnoreCase(communityPatchDTO.getName())){
            community.setName(communityPatchDTO.getName());
        }
        if(communityPatchDTO.getDescription()!=null && !community.getDescription().equalsIgnoreCase(communityPatchDTO.getDescription())){
            community.setDescription(communityPatchDTO.getDescription());
        }

        if(communityPatchDTO.getOwnerName()!=null) {
            User newOwner = userRepo.findByUsername(communityPatchDTO.getOwnerName())
                    .orElseThrow(() -> new DoesNotExistException("User with that name does not exist"));

            if (community.getOwner().getId() != newOwner.getId()) {
                community.setOwner(newOwner);
            }
        }
        communityRepo.save(community);

        CommunityResponseDTO communityResponseDTO = communityToDTO(community);
        return communityResponseDTO;
    }

    public CommunityResponseDTO getCommunity(Long communityId){
        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new DoesNotExistException("Community does not exist "));
        CommunityResponseDTO communityResponseDTO = communityToDTO(community);
        return communityResponseDTO;
    }
    public List<CommunityResponseDTO> getCommunities(){
        List<Community> communities = communityRepo.findAll();
        List<CommunityResponseDTO> communityResponseDTOList = new ArrayList<>();
        for (Community c : communities) {
            communityResponseDTOList.add(communityToDTO(c));
        }
        return communityResponseDTOList;
    }

    public List<PostResponseDTO> getPostList(Long communityId){
        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new DoesNotExistException("Community does not exist "));

        List<Post> posts = community.getPostList();
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        for (Post p : posts) {
            postResponseDTOList.add(postService.postToDTO(p));
        }
        return postResponseDTOList;
    }



}
