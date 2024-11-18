package com.titans.ahs.service.impl;

import com.titans.ahs.exception.type.BadRequestException;
import com.titans.ahs.model.MedicalChat;
import com.titans.ahs.model.Page;
import com.titans.ahs.model.dto.MedicalChatDate;
import com.titans.ahs.repository.MedicalChatRepository;
import com.titans.ahs.service.MedicalChatService;
import io.micrometer.common.util.StringUtils;
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

    @Override
    public MedicalChat createMedicalChat(MedicalChat medicalChat) {
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
