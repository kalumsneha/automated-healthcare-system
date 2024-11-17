package com.titans.ahs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.titans.ahs.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "medical_chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicalChat extends BaseEntity {

    @Column(name = "reference_number", nullable = false)
    private String referenceNumber;

    @Column(name = "user_id", nullable = false)
    @NotEmpty(message = "userId is required")
    private String userId;

    @Column(name = "chat_date_time", nullable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "chat_message", nullable = false)
    @NotEmpty(message = "chatMessage is required")
    private String chatMessage;


}
