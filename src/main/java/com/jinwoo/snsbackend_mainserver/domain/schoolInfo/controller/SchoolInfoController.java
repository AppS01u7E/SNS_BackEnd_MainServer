package com.jinwoo.snsbackend_mainserver.domain.schoolInfo.controller;


import com.jinwoo.snsbackend_mainserver.domain.schoolInfo.exception.SchoolInfoException;
import com.jinwoo.snsbackend_mainserver.domain.schoolInfo.payload.LocalSchoolShortenResponse;
import com.jinwoo.snsbackend_mainserver.domain.schoolInfo.service.SchoolInfoService;
import com.jinwoo.snsbackend_mainserver.global.exception.NeisApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/school")
@RestController
public class SchoolInfoController {
    private final SchoolInfoService schoolInfoService;


    @GetMapping("/search")
    public List<LocalSchoolShortenResponse> searchSchool(@RequestParam String school){
        try {
            return schoolInfoService.getSchoolInfo(school);
        } catch (IOException e) {
            throw new SchoolInfoException(e.getMessage());
        } catch (Exception e){
            throw new NeisApiException(e.getMessage());
        }
    }



//    @GetMapping("/food")
}
