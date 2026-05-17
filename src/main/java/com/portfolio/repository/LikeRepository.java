package com.portfolio.repository;

import com.portfolio.entity.Like;
import com.portfolio.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMediaAndIpAddress(Media media, String ipAddress);
    boolean existsByMediaAndIpAddress(Media media, String ipAddress);
    long countByMedia(Media media);
}
