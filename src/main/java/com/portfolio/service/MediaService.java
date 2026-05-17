package com.portfolio.service;

import com.portfolio.dto.MediaDTO;
import com.portfolio.entity.Category;
import com.portfolio.entity.Media;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.CategoryRepository;
import com.portfolio.repository.MediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MediaService {

    @Autowired private MediaRepository mediaRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private FileUploadService fileUploadService;

    public Page<MediaDTO> getAllMedia(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return mediaRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<MediaDTO> getMediaByCategory(Long categoryId, int page, int size) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return mediaRepository.findByCategory(category, pageable).map(this::toDTO);
    }

    public Page<MediaDTO> searchMedia(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return mediaRepository.searchMedia(query, pageable).map(this::toDTO);
    }

    public MediaDTO getMediaById(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));
        return toDTO(media);
    }

    public List<MediaDTO> getFeaturedMedia() {
        return mediaRepository.findByIsFeaturedTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MediaDTO> getTrendingMedia() {
        return mediaRepository.findByIsTrendingTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MediaDTO createMedia(MediaDTO dto, MultipartFile videoFile, MultipartFile thumbnailFile) throws IOException {
        Media media = new Media();
        updateMediaFromDTO(media, dto);
        if (videoFile != null && !videoFile.isEmpty()) {
            media.setFilePath(fileUploadService.uploadFile(videoFile, "portfolio/media"));
        }
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            media.setThumbnailPath(fileUploadService.uploadFile(thumbnailFile, "portfolio/thumbnails"));
        }
        return toDTO(mediaRepository.save(media));
    }

    public MediaDTO updateMedia(Long id, MediaDTO dto, MultipartFile videoFile, MultipartFile thumbnailFile) throws IOException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));
        updateMediaFromDTO(media, dto);
        if (videoFile != null && !videoFile.isEmpty()) {
            media.setFilePath(fileUploadService.uploadFile(videoFile, "portfolio/media"));
        }
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            media.setThumbnailPath(fileUploadService.uploadFile(thumbnailFile, "portfolio/thumbnails"));
        }
        return toDTO(mediaRepository.save(media));
    }

    public void deleteMedia(Long id) {
        if (!mediaRepository.existsById(id)) throw new ResourceNotFoundException("Media not found");
        mediaRepository.deleteById(id);
    }

    public void incrementView(Long id) {
        mediaRepository.incrementViewCount(id);
    }

    private void updateMediaFromDTO(Media media, MediaDTO dto) {
        media.setTitle(dto.getTitle());
        media.setDescription(dto.getDescription());
        media.setVideoUrl(dto.getVideoUrl());
        media.setMediaType(dto.getMediaType() != null ? dto.getMediaType() : Media.MediaType.VIDEO);
        media.setIsFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false);
        media.setIsTrending(dto.getIsTrending() != null ? dto.getIsTrending() : false);
        media.setTags(dto.getTags());
        media.setDuration(dto.getDuration());
        media.setClientName(dto.getClientName());
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId()).ifPresent(media::setCategory);
        }
    }



    private MediaDTO toDTO(Media media) {
        MediaDTO dto = modelMapper.map(media, MediaDTO.class);
        if (media.getCategory() != null) {
            dto.setCategoryId(media.getCategory().getId());
            dto.setCategoryName(media.getCategory().getName());
        }
        // Encode spaces in file paths so URLs are valid (handles legacy files with spaces in names)
        if (dto.getFilePath() != null)
            dto.setFilePath(dto.getFilePath().replace(" ", "%20"));
        if (dto.getThumbnailPath() != null)
            dto.setThumbnailPath(dto.getThumbnailPath().replace(" ", "%20"));
        dto.setCommentCount(media.getComments() != null ? 
            (int) media.getComments().stream().filter(c -> c.getIsApproved()).count() : 0);
        return dto;
    }
}
