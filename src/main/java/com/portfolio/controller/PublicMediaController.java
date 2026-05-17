package com.portfolio.controller;

import com.portfolio.dto.*;
import com.portfolio.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/media")
public class PublicMediaController {

    @Autowired private MediaService mediaService;
    @Autowired private CommentService commentService;
    @Autowired private LikeService likeService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MediaDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        return ResponseEntity.ok(ApiResponse.success(mediaService.getAllMedia(page, size, sortBy)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(mediaService.getMediaById(id)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<MediaDTO>>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(ApiResponse.success(mediaService.getMediaByCategory(categoryId, page, size)));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<MediaDTO>>> getFeatured() {
        return ResponseEntity.ok(ApiResponse.success(mediaService.getFeaturedMedia()));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<MediaDTO>>> getTrending() {
        return ResponseEntity.ok(ApiResponse.success(mediaService.getTrendingMedia()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<MediaDTO>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(ApiResponse.success(mediaService.searchMedia(q, page, size)));
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<ApiResponse<String>> incrementView(@PathVariable Long id) {
        mediaService.incrementView(id);
        return ResponseEntity.ok(ApiResponse.success("View recorded", "OK"));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Map<String, Object>>> like(
            @PathVariable Long id, HttpServletRequest request) {
        String ip = getClientIp(request);
        return ResponseEntity.ok(ApiResponse.success(likeService.toggleLike(id, ip)));
    }

    @GetMapping("/{id}/liked")
    public ResponseEntity<ApiResponse<Boolean>> hasLiked(
            @PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success(likeService.hasLiked(id, getClientIp(request))));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getApprovedComments(id)));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<ApiResponse<CommentDTO>> addComment(
            @PathVariable Long id, @RequestBody CommentDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Comment submitted for review", commentService.addComment(id, dto)));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip != null && !ip.isEmpty()) ? ip.split(",")[0] : request.getRemoteAddr();
    }
}
