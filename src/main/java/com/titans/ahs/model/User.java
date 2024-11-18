package com.titans.ahs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.titans.ahs.model.base.BaseEntity;
import com.titans.ahs.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    @NotEmpty(message = "username is required")
    private String username;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "password is required")
    private String password;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "firstName is required")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "lastName is required")
    private String lastName;

    /*@Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;*/

    @Column(name = "phone_number", nullable = false)
    @NotEmpty(message = "phoneNumber is required")
    private String phoneNumber;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "role", nullable = false)
    private Role role;



}
