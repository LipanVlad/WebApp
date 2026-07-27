package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.*;
import com.example.webApp.Services.CommunityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommunityController {
    private final CommunityService service;
    public CommunityController(CommunityService communityService){
        this.service = communityService;
    }

    @PostMapping("/add/community")
    public ResponseEntity<?> addCommunity(@Valid @RequestBody CommunityRequestDTO communityRequestDTO){
         CommunityResponseDTO communityResponseDTO = service.checkAndSaveCommunity(communityRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(communityResponseDTO);
    }

    @DeleteMapping("/communities/{communityId}")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long communityId) {
        service.deleteCommunity(communityId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/communities/{currentName}")
    public ResponseEntity<?> patchCommunity(@RequestBody CommunityPatchDTO communityPatchDTO, @PathVariable String currentName){
        CommunityResponseDTO communityResponseDTO = service.patchCommunity(communityPatchDTO, currentName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(communityResponseDTO);
    }
    @GetMapping("/communities/{communityId}")
    public ResponseEntity<?> getCommunity(@PathVariable Long communityId) {
        CommunityResponseDTO communityResponseDTO = service.getCommunity(communityId);
        return ResponseEntity.status(HttpStatus.OK).body(communityResponseDTO);
    }
    @GetMapping("/communities")
    public ResponseEntity<?> getCommunities() {
        List<CommunityResponseDTO> communityResponseDTOList = service.getCommunities();
        return ResponseEntity.status(HttpStatus.OK).body(communityResponseDTOList);
    }

    @GetMapping("/communities/{communityId}/posts")
    public ResponseEntity<?> getPostList(@PathVariable Long communityId){
        List<PostResponseDTO> postResponseDTOList = service.getPostList(communityId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDTOList);
    }
}
