package com.portfolio.controller;

import com.portfolio.dto.*;
import com.portfolio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {
    @Autowired private MediaRepository mediaRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private LikeRepository likeRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalMedia(mediaRepository.count());
        stats.setTotalComments(commentRepository.count());
        stats.setPendingComments(commentRepository.countByIsApprovedFalse());
        stats.setFeaturedMedia(mediaRepository.findByIsFeaturedTrue().size());
        
        long totalViews = mediaRepository.findAll().stream().mapToLong(m -> m.getViewCount() != null ? m.getViewCount() : 0).sum();
        long totalLikes = mediaRepository.findAll().stream().mapToLong(m -> m.getLikeCount() != null ? m.getLikeCount() : 0).sum();
        stats.setTotalViews(totalViews);
        stats.setTotalLikes(totalLikes);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
