package com.dic2.investeasy.service;

import com.dic2.investeasy.domain.Comment;
import com.dic2.investeasy.domain.User;
import com.dic2.investeasy.domain.Video;
import com.dic2.investeasy.dto.comment.CommentCreateDTO;
import com.dic2.investeasy.dto.comment.CommentUpdateDTO;
import com.dic2.investeasy.repository.CommentRepository;
import com.dic2.investeasy.repository.UserRepository;
import com.dic2.investeasy.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    public Page<Comment> getCommentsByVideoId(String videoId, Pageable pageable) {
        return commentRepository.findByVideoId(videoId, pageable);
    }

    public Page<Comment> getRepliesByParentId(String parentId, Pageable pageable) {
        return commentRepository.findByParentId(parentId, pageable);
    }

    public Page<Comment> getReportedComments(Pageable pageable) {
        return commentRepository.findByReportedTrue(pageable);
    }

    @Transactional
    public Comment createComment(String userId, CommentCreateDTO dto) {
        User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setUser(user);

        if (dto.getVideoId() != null) {
            Video video = videoRepository.findById(dto.getVideoId())
                    .orElseThrow(() -> new RuntimeException("Video not found"));
            comment.setVideo(video);
        }

        if (dto.getParentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParent(parent);
        }

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(String id, String userId, CommentUpdateDTO dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to update this comment");
        }

        comment.setText(dto.getText());
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(String id, String userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public Comment toggleLike(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikes(comment.getLikes() + 1);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment reportComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setReported(true);
        comment.setReportCount(comment.getReportCount() + 1);
        return commentRepository.save(comment);
    }
}