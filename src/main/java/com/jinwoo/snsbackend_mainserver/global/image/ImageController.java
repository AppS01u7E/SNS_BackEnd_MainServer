package com.jinwoo.snsbackend_mainserver.global.controller;


import com.jinwoo.snsbackend_mainserver.global.firebase.service.FcmService;
import com.jinwoo.snsbackend_mainserver.global.utils.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageController {
    private final S3Util s3Util;


    @PostMapping
    public ResponseEntity<?> getImageURL(@RequestBody ImageRequest request){
        return ResponseEntity.ok().body(s3Util.getURL(request.getFileKey()));
    }


}
