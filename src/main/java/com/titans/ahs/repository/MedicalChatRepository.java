package com.titans.ahs.repository;

import com.titans.ahs.model.MedicalChat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalChatRepository extends JpaRepository<MedicalChat, Long> {
    List<MedicalChat> findAllByUserId(String userId, Pageable pageable);

    Integer countByUserId(String userId);
    
    List<MedicalChat> findAllByUserIdAndCreatedDateTimeBetween(String userId, LocalDateTime createdDateTimeStart, LocalDateTime createdDateTimeEnd, PageRequest createdDateTime);

    Integer countByUserIdAndCreatedDateTimeBetween(String userId, LocalDateTime createdDateTimeStart, LocalDateTime createdDateTimeEnd);
}
