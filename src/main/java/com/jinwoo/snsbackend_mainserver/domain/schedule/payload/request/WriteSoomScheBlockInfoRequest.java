package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class WriteSoomScheBlockInfoRequest {
    @NotNull(message = "날짜가 비어서는 안됩니다.")
    private LocalDate date;
    @NotNull(message = "교시가 비어서는 안됩니다.")
    private int period;

    @Size(max = 15, message = "제목은 15자를 넘을 수 없습니다.")
    @NotNull(message = "제목이 비어서는 안됩니다.")
    private String title;
    private String info;

}
