package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GeneSoomRequest {
    @NotNull(message = "title은 비어서는 안됩니다.")
    private String title;
    private String info;
}
