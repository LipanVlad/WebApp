package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.CommunityRequestDTO;
import com.example.webApp.DataTransferObjects.CommunityResponseDTO;
import com.example.webApp.Services.CommunityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
