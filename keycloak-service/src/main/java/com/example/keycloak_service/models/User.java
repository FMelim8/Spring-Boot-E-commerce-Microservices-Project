package com.example.keycloak_service.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users" ,indexes = {
        @Index(name = "idx_sub", columnList = "subject")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;
}
