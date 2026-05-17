package com.portfolio.controller;

import com.portfolio.dto.*;
import com.portfolio.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/comments")
public class AdminCommentController {
    @Autowired private CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean approved) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getAllComments(page, size, approved)));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<CommentDTO>> approve(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Comment approved", commentService.approveComment(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted", "OK"));
    }
}
