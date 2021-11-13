package com.jinwoo.snsbackend_mainserver.domain.schoolInfo.service;


import com.jinwoo.snsbackend_mainserver.domain.schoolInfo.payload.LocalSchoolShortenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neiseApi.School;
import neiseApi.payload.schoolInfo.SchoolShorten;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchoolInfoServiceImpl implements SchoolInfoService{

    @Value("${neis.serviceKey}")
    private String serviceKey;

    public List<LocalSchoolShortenResponse> getSchoolInfo(String schoolName) throws IOException {
        neiseApi.School school = new School(serviceKey);
        return school.getSchoolDetailInfo(schoolName).stream().map(
                schoolShorten -> new LocalSchoolShortenResponse(schoolShorten.getCode(), schoolShorten.getName(), schoolShorten.getAreaCode()
                , schoolShorten.getAddressCode(), schoolShorten.getHomePage(), schoolShorten.getTelephone(),
                        schoolShorten.getType(), schoolShorten.getKind().toString())
        ).collect(Collectors.toList());
    }


}
