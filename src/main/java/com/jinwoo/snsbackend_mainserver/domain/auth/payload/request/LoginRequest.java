package com.jinwoo.snsbackend_mainserver.domain.auth.payload.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class LoginRequest {
    private String id;
    private String password;
    private String deviceToken;
}
