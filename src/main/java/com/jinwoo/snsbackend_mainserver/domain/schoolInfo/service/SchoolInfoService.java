package com.jinwoo.snsbackend_mainserver.domain.schoolInfo.service;

import com.jinwoo.snsbackend_mainserver.domain.schoolInfo.payload.LocalSchoolShortenResponse;

import java.io.IOException;
import java.util.List;

public interface SchoolInfoService {


    public List<LocalSchoolShortenResponse> getSchoolInfo(String schoolName) throws IOException;
}
