package com.example.keycloak_service.repository;

import com.example.keycloak_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean findBySubject(String subject);

    boolean existsUserBySubject(String id);

    List<User> findAllById(Iterable<Long> userIds);

    List<?> findUserBySubject(String userFromJwt);
}
