package com.titans.ahs.repository;

import com.titans.ahs.model.MedicalChat;
import com.titans.ahs.model.dto.MedicalChatDate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalChatRepository extends JpaRepository<MedicalChat, Long> {
    List<MedicalChat> findAllByUserId(String userId, Pageable pageable);

    Integer countByUserId(String userId);
    
    List<MedicalChat> findAllByUserIdAndCreatedDateTimeBetween(String userId, LocalDateTime createdDateTimeStart, LocalDateTime createdDateTimeEnd, PageRequest createdDateTime);

    Integer countByUserIdAndCreatedDateTimeBetween(String userId, LocalDateTime createdDateTimeStart, LocalDateTime createdDateTimeEnd);

    @Query("select new com.titans.ahs.model.dto.MedicalChatDate(cast(createdDateTime as LocalDate) as createdDate, count(cast(createdDateTime as LocalDate)) as count) from MedicalChat where userId = :userId group by cast(createdDateTime as LocalDate)")
    List<MedicalChatDate> findAllByUserIdGroupByDates(@Param("userId") String userId);
}
