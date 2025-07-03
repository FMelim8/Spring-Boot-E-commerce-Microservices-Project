package com.example.gameorder.payloads.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class UserRequest {

    private Long id;

    private String subject;

    private String username;

    private String email;
}
