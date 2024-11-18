package com.titans.ahs.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titans.ahs.model.MedicalChat;
import com.titans.ahs.model.Page;
import com.titans.ahs.service.MedicalChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical-chat")
@Slf4j
public class MedicalChatController {

    @Autowired
    private MedicalChatService medicalChatService;

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public MedicalChat createMedicalChat(@Validated @RequestBody MedicalChat medicalChat) throws JsonProcessingException {
        log.info("Create Medical Chat Request: {}", new ObjectMapper().writeValueAsString(medicalChat));

        return this.medicalChatService.createMedicalChat(medicalChat);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/{userId}")
    public Page<MedicalChat> getMedicalChats(
            @PathVariable String userId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String createdDateTimeStart,
            @RequestParam(required = false) String createdDateTimeEnd) throws JsonProcessingException {
        log.info("Fetching Medical Chats By User ID: {}, Page Number: {}, Page Size: {}, Created Date Time Start: {}, Created Date Time End: {}", userId, pageNumber, pageSize, createdDateTimeStart, createdDateTimeEnd);
        return this.medicalChatService.getMedicalChats(userId, pageNumber, pageSize, createdDateTimeStart, createdDateTimeEnd);
    }

}
