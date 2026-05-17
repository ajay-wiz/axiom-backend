package com.portfolio.dto;

import com.portfolio.entity.Media;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MediaDTO {
    private Long id;
    private String title;
    private String description;
    private String filePath;
    private String thumbnailPath;
    private String videoUrl;
    private Media.MediaType mediaType;
    private Long categoryId;
    private String categoryName;
    private Long viewCount;
    private Long likeCount;
    private Boolean isFeatured;
    private Boolean isTrending;
    private String tags;
    private String duration;
    private String clientName;
    private LocalDateTime createdAt;
    private int commentCount;
}
