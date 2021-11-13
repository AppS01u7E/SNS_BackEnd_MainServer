package com.jinwoo.snsbackend_mainserver.domain.schoolInfo.payload;


import lombok.*;
import neiseApi.payload.sche.SchoolType;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalSchoolShortenResponse {

    private String code;
    private String name;
    private String areaCode;
    private String addressCode;
    private String homePage;
    private String telephone;
    private String type;
    private SchoolTypeDto kind;

    public LocalSchoolShortenResponse(String code, String name, String areaCode
            , String addressCode, String homePage, String telephone
            , String type, String kind) {
        this.code = code;
        this.name = name;
        this.areaCode = areaCode;
        this.addressCode = addressCode;
        this.homePage = homePage;
        this.telephone = telephone;
        this.type = type;
        if (kind.equals("HIGH")) this.kind = SchoolTypeDto.HIGH;
        else if(kind.equals("MIDDLE")) this.kind = SchoolTypeDto.MIDDLE;
        else this.kind = SchoolTypeDto.ELEMENT;
    }
}

