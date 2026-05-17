package com.portfolio.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private Long mediaId;
    private String authorName;
    private String authorEmail;
    private String content;
    private Boolean isApproved;
    private LocalDateTime createdAt;
}
