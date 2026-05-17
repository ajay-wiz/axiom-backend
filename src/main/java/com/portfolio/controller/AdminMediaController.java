package com.portfolio.controller;

import com.portfolio.dto.*;
import com.portfolio.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/admin/media")
public class AdminMediaController {

    @Autowired private MediaService mediaService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MediaDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(mediaService.getAllMedia(page, size, "createdAt")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MediaDTO>> create(
            @RequestPart("data") MediaDTO dto,
            @RequestPart(value = "video", required = false) MultipartFile video,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        return ResponseEntity.ok(ApiResponse.success("Media created", mediaService.createMedia(dto, video, thumbnail)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaDTO>> update(
            @PathVariable Long id,
            @RequestPart("data") MediaDTO dto,
            @RequestPart(value = "video", required = false) MultipartFile video,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        return ResponseEntity.ok(ApiResponse.success("Media updated", mediaService.updateMedia(id, dto, video, thumbnail)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.ok(ApiResponse.success("Media deleted", "OK"));
    }
}
