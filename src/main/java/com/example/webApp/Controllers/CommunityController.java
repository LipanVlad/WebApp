package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.CommunityRequestDTO;
import com.example.webApp.DataTransferObjects.CommunityResponseDTO;
import com.example.webApp.Services.CommunityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunityController {
    private final CommunityService service;
    public CommunityController(CommunityService communityService){
        this.service = communityService;
    }

    @PostMapping("/add/community")
    public ResponseEntity<?> addCommunity(@RequestBody CommunityRequestDTO communityRequestDTO){
         CommunityResponseDTO communityResponseDTO = service.checkAndSaveCommunity(communityRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(communityResponseDTO);
    }
}
