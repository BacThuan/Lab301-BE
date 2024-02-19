package com.application.backend.controller;

import com.application.backend.file.ImageHandler;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
public class ProductImageController {
    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            // Tạo đường dẫn đến file ảnh
            Resource resource = ImageHandler.loadImage(imageName);

            // Kiểm tra xem tệp có tồn tại không
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Hoặc MediaType.IMAGE_PNG nếu bạn sử dụng ảnh PNG
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
