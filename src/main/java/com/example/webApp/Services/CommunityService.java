package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.CommunityRequestDTO;
import com.example.webApp.DataTransferObjects.CommunityResponseDTO;
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


@Service
public class CommunityService {
    private final CommunityRepo communityRepo;
    private final UserRepo userRepo;
    public CommunityService(CommunityRepo communityRepo, UserRepo userRepo){
        this.communityRepo = communityRepo;
        this.userRepo = userRepo;

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
        if(communityRequestDTO.getName() == null || communityRequestDTO.getName().isEmpty()){
            throw new InvalidInputException("Community must have a valid name");
        }

        if(communityRequestDTO.getDescription() == null || communityRequestDTO.getDescription().isEmpty()){
            throw new InvalidInputException("Community must have a valid description");
        }
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
}
