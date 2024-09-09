package com.titans.ahs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "AHS_USER_INFO")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotEmpty(message = "username is required")
    private String username;

    @Column(name = "firstname", nullable = false)
    @NotEmpty(message = "firstName is required")
    private String firstName;

    @Column(name = "lastname", nullable = false)
    @NotEmpty(message = "lastName is required")
    private String lastName;

}
