package com.portfolio.service;

import com.portfolio.dto.CommentDTO;
import com.portfolio.entity.Comment;
import com.portfolio.entity.Media;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.CommentRepository;
import com.portfolio.repository.MediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    @Autowired private CommentRepository commentRepository;
    @Autowired private MediaRepository mediaRepository;
    @Autowired private ModelMapper modelMapper;

    public CommentDTO addComment(Long mediaId, CommentDTO dto) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));
        Comment comment = new Comment();
        comment.setMedia(media);
        comment.setAuthorName(dto.getAuthorName());
        comment.setAuthorEmail(dto.getAuthorEmail());
        comment.setContent(dto.getContent());
        comment.setIsApproved(false);
        Comment saved = commentRepository.save(comment);
        return toDTO(saved);
    }

    public List<CommentDTO> getApprovedComments(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));
        return commentRepository.findByMediaAndIsApprovedTrue(media)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Page<CommentDTO> getAllComments(int page, int size, Boolean approved) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (approved != null) return commentRepository.findByIsApproved(approved, pr).map(this::toDTO);
        return commentRepository.findAll(pr).map(this::toDTO);
    }

    public CommentDTO approveComment(Long id) {
        Comment c = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        c.setIsApproved(true);
        return toDTO(commentRepository.save(c));
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) throw new ResourceNotFoundException("Comment not found");
        commentRepository.deleteById(id);
    }

    private CommentDTO toDTO(Comment c) {
        CommentDTO dto = modelMapper.map(c, CommentDTO.class);
        dto.setMediaId(c.getMedia().getId());
        return dto;
    }
}
