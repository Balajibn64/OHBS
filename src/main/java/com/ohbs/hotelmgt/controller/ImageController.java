package com.ohbs.hotelmgt.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ohbs.hotelmgt.service.ImageService;

@RestController
@RequestMapping("/hotel/images")
public class ImageController {

    @Autowired
    private ImageService imageService;
    
    @PostMapping("/upload/{hotelId}")
    public ResponseEntity<?> uploadImages(@PathVariable Long hotelId,
                                          @RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> imageUrls = imageService.uploadImages(files, hotelId);
        return ResponseEntity.ok(Map.of("uploaded", imageUrls));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<?> getImages(@PathVariable Long hotelId) {
        List<String> imageUrls = imageService.getImagesByHotelId(hotelId);
        return ResponseEntity.ok(Map.of("images", imageUrls));
    }

    @PutMapping("/update/{imageId}")
    public ResponseEntity<?> updateImage(@PathVariable Long imageId,
                                         @RequestParam("files") MultipartFile files) {
        try {
            String updatedUrl = imageService.updateImage(imageId, files);
            return ResponseEntity.ok(Map.of("updatedImageUrl", updatedUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        try {
            imageService.deleteImage(imageId);
            return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
