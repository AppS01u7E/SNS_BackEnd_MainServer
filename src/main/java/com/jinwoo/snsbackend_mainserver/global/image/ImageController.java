package com.jinwoo.snsbackend_mainserver.global.image;


import com.jinwoo.snsbackend_mainserver.global.utils.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageController {
    private final S3Util s3Util;


    @PostMapping
    public ImageUrlResponse getImageURL(@RequestBody ImageRequest request){
        return s3Util.getURL(request.getFileKey());
    }


}
