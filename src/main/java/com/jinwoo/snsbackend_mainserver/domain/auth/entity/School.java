package com.jinwoo.snsbackend_mainserver.domain.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

public enum School {
    DAEDOK,
    BUSAN,
    DAEGU,
    GWANGJU,
    ELSE
}
