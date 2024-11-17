package com.titans.ahs.repository;

import com.titans.ahs.model.MedicalChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalChatRepository extends JpaRepository<MedicalChat, Long> {
    List<MedicalChat> findAllByUserId(String userId, Pageable pageable);

    Integer countByUserId(String userId);
}
