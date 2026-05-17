package com.portfolio.dto;

import lombok.Data;

@Data
public class DashboardStats {
    private long totalMedia;
    private long totalViews;
    private long totalLikes;
    private long totalComments;
    private long pendingComments;
    private long featuredMedia;
}
