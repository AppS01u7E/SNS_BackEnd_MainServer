package com.jinwoo.snsbackend_mainserver.global.email.payload;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailSendRequest {
    private String email;
}
