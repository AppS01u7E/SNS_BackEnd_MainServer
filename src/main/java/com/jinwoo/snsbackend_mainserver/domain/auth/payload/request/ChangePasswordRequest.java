package com.jinwoo.snsbackend_mainserver.domain.auth.payload.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String code;
    private String password;

}
