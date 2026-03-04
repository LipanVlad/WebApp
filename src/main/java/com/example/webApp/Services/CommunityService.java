package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.CommunityDTO;
import com.example.webApp.Entities.Community;
import com.example.webApp.Entities.User;
import com.example.webApp.Exceptions.DoesNotExistException;
import com.example.webApp.Exceptions.InvalidInputException;
import com.example.webApp.Exceptions.NameAlreadyExistsException;
import com.example.webApp.Repositories.CommunityRepo;
import com.example.webApp.Repositories.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class CommunityService {
    private final CommunityRepo communityRepo;
    private final UserRepo userRepo;
    public CommunityService(CommunityRepo communityRepo, UserRepo userRepo){
        this.communityRepo = communityRepo;
        this.userRepo = userRepo;

    }
    public void checkAndSaveCommunity(CommunityDTO communityDTO){
        if(communityDTO.getName() == null || communityDTO.getName().isEmpty()){
            throw new InvalidInputException("Community must have a valid name");
        }
        if(communityRepo.findByName(communityDTO.getName()).isPresent()){
            throw new NameAlreadyExistsException("Community name is taken");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();
        Community community = new Community();
        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());
        User owner = userRepo.findByUsername(loggedUserName)
                .orElseThrow(() -> new DoesNotExistException("User is not logged in anymore"));
        community.setOwner(owner);
        communityRepo.save(community);
    }
}
