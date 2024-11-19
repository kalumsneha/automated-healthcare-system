package com.titans.ahs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titans.ahs.exception.type.BadRequestException;
import com.titans.ahs.model.MedicalChat;
import com.titans.ahs.model.Page;
import com.titans.ahs.model.dto.MedicalChatDate;
import com.titans.ahs.model.llama.LlamaMessage;
import com.titans.ahs.model.llama.LlamaResponse;
import com.titans.ahs.repository.MedicalChatRepository;
import com.titans.ahs.service.MedicalChatService;
import com.titans.ahs.service.chatgpt.ChatService;
import com.titans.ahs.service.llama.LlamaAiService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MedicalChatServiceImpl implements MedicalChatService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private MedicalChatRepository medicalChatRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private LlamaAiService llamaAiService;

    @Override
    @Transactional
    public MedicalChat createMedicalChat(MedicalChat medicalChat) throws JsonProcessingException {
        medicalChat.setReferenceNumber(UUID.randomUUID().toString());
        medicalChat.setCreatedDateTime(LocalDateTime.now());

        this.medicalChatRepository.save(medicalChat);

        return this.createMedicalChatResponse(medicalChat);
    }

    public MedicalChat createMedicalChatResponse(MedicalChat medicalChat) throws JsonProcessingException {

        //Chat GPT 3.5 Open AI
        //String chatResponse = this.chatService.chat(medicalChat.getChatMessage());

        //Ollama
        medicalChat.getChatMessage().concat(" .Make it brief and highlight some remedies. ");
        LlamaResponse chatResponse = this.llamaAiService.generateMessage(medicalChat.getChatMessage());
        LlamaMessage responseMessage = new ObjectMapper().readValue(chatResponse.getMessage(), LlamaMessage.class);
        medicalChat.setChatMessage(StringUtils.isEmpty(responseMessage.getResponse()) ? "No Response. Please try again.": responseMessage.getResponse());
        medicalChat.setReferenceNumber(UUID.randomUUID().toString());
        medicalChat.setCreatedDateTime(LocalDateTime.now());
        return this.medicalChatRepository.save(medicalChat);
    }

    @Override
    public Page<MedicalChat> getMedicalChats(String userId, Integer pageNumber, Integer pageSize, String createdDateTimeStartStr, String createdDateTimeEndStr) {
        if(pageNumber == null || pageNumber < 0)
            pageNumber = 0;
        if(pageSize == null || pageSize <= 0)
            pageSize = 100;
        LocalDateTime createdDateTimeStart = null;
        LocalDateTime createdDateTimeEnd = null;
        try{
            if(!StringUtils.isEmpty(createdDateTimeStartStr)){
                createdDateTimeStart = LocalDateTime.parse(createdDateTimeStartStr, dateTimeFormatter);
            }
            if(!StringUtils.isEmpty(createdDateTimeEndStr)){
                createdDateTimeEnd = LocalDateTime.parse(createdDateTimeEndStr, dateTimeFormatter);
            }

            if(createdDateTimeStart != null && createdDateTimeEnd != null &&
                    createdDateTimeStart.isAfter(createdDateTimeEnd))
                throw new BadRequestException("Created Date Time Start Cannot Be After Created Date Time End");

            if(createdDateTimeStart != null && createdDateTimeEnd == null)
                createdDateTimeEnd = LocalDateTime.now();

            if(createdDateTimeEnd != null && createdDateTimeStart == null)
                createdDateTimeStart = createdDateTimeEnd.minusYears(1);

        }
        catch(DateTimeParseException ex){
            log.error(ex.getMessage());
            throw new BadRequestException("Unable to parse provided created date times");
        }

        List<MedicalChat> medicalChats;
        Integer count;
        if(createdDateTimeStart == null && createdDateTimeEnd == null){
            medicalChats = this.medicalChatRepository.findAllByUserId(userId, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDateTime")));
            count = this.medicalChatRepository.countByUserId(userId);
        }
        else{
            medicalChats = this.medicalChatRepository.findAllByUserIdAndCreatedDateTimeBetween(userId, createdDateTimeStart, createdDateTimeEnd, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDateTime")));
            count = this.medicalChatRepository.countByUserIdAndCreatedDateTimeBetween(userId, createdDateTimeStart, createdDateTimeEnd);
        }
        return new Page<>(medicalChats, count);
    }

    public List<MedicalChatDate> getDistinctDatesFromMedicalChats(String userId){
         return this.medicalChatRepository.findAllByUserIdGroupByDates(userId);
    }
}
