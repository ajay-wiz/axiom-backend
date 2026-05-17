package com.portfolio.repository;

import com.portfolio.entity.Comment;
import com.portfolio.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMediaAndIsApprovedTrue(Media media);
    Page<Comment> findAll(Pageable pageable);
    Page<Comment> findByIsApproved(Boolean approved, Pageable pageable);
    long countByIsApprovedFalse();
}
