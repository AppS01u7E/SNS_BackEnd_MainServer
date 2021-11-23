package com.jinwoo.snsbackend_mainserver.domain.auth.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchQueryRequest {

    private String soomId;
    private int classNum;
    private int grade;

}
