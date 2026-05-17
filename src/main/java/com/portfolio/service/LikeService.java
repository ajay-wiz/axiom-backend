package com.portfolio.service;

import com.portfolio.entity.Like;
import com.portfolio.entity.Media;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.LikeRepository;
import com.portfolio.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@Transactional
public class LikeService {
    @Autowired private LikeRepository likeRepository;
    @Autowired private MediaRepository mediaRepository;

    public Map<String, Object> toggleLike(Long mediaId, String ipAddress) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));
        boolean alreadyLiked = likeRepository.existsByMediaAndIpAddress(media, ipAddress);
        if (alreadyLiked) {
            likeRepository.findByMediaAndIpAddress(media, ipAddress)
                    .ifPresent(likeRepository::delete);
            media.setLikeCount(Math.max(0, media.getLikeCount() - 1));
        } else {
            Like like = new Like();
            like.setMedia(media);
            like.setIpAddress(ipAddress);
            likeRepository.save(like);
            media.setLikeCount(media.getLikeCount() + 1);
        }
        mediaRepository.save(media);
        return Map.of("liked", !alreadyLiked, "likeCount", media.getLikeCount());
    }

    public boolean hasLiked(Long mediaId, String ipAddress) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));
        return likeRepository.existsByMediaAndIpAddress(media, ipAddress);
    }
}
