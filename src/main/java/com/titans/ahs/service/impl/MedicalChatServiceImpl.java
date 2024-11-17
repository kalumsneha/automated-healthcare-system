package com.titans.ahs.service.impl;

import com.titans.ahs.model.MedicalChat;
import com.titans.ahs.model.Page;
import com.titans.ahs.repository.MedicalChatRepository;
import com.titans.ahs.service.MedicalChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MedicalChatServiceImpl implements MedicalChatService {

    @Autowired
    private MedicalChatRepository medicalChatRepository;

    @Override
    public MedicalChat createMedicalChat(MedicalChat medicalChat) {
        medicalChat.setReferenceNumber(UUID.randomUUID().toString());
        medicalChat.setCreatedDateTime(LocalDateTime.now());
        return this.medicalChatRepository.save(medicalChat);
    }

    @Override
    public Page<MedicalChat> getMedicalChats(String userId, Integer pageNumber, Integer pageSize) {
        if(pageNumber == null || pageNumber < 0)
            pageNumber = 0;
        if(pageSize == null || pageSize <= 0)
            pageSize = 100;
        List<MedicalChat> medicalChats = this.medicalChatRepository.findAllByUserId(userId, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDateTime")));
        Integer count = this.medicalChatRepository.countByUserId(userId);
        return new Page<>(medicalChats, count);
    }
}
