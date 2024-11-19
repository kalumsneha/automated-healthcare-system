package com.titans.ahs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.titans.ahs.model.MedicalChat;
import com.titans.ahs.model.Page;
import com.titans.ahs.model.dto.MedicalChatDate;

import java.util.List;

public interface MedicalChatService {
    MedicalChat createMedicalChat(MedicalChat medicalChat) throws JsonProcessingException;

    Page<MedicalChat> getMedicalChats(String userId, Integer pageNumber, Integer pageSize, String createdDateTimeStart, String createdDateTimeEnd);

    List<MedicalChatDate> getDistinctDatesFromMedicalChats(String userId);
}
