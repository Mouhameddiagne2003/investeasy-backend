package com.dic2.investeasy.controller;

import com.dic2.investeasy.domain.Comment;
import com.dic2.investeasy.dto.comment.CommentCreateDTO;
import com.dic2.investeasy.dto.comment.CommentUpdateDTO;
import com.dic2.investeasy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<Comment>> getComments(
            @RequestParam(required = false) String videoId,
            Pageable pageable) {
        if (videoId != null) {
            return ResponseEntity.ok(commentService.getCommentsByVideoId(videoId, pageable));
        }
        return ResponseEntity.ok(commentService.getAllComments(pageable));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<Page<Comment>> getReplies(@PathVariable String id, Pageable pageable) {
        return ResponseEntity.ok(commentService.getRepliesByParentId(id, pageable));
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentCreateDTO dto) {
        return ResponseEntity.ok(commentService.createComment(userDetails.getUsername(), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentUpdateDTO dto) {
        return ResponseEntity.ok(commentService.updateComment(id, userDetails.getUsername(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Comment> toggleLike(@PathVariable String id) {
        return ResponseEntity.ok(commentService.toggleLike(id));
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<Comment> reportComment(@PathVariable String id) {
        return ResponseEntity.ok(commentService.reportComment(id));
    }
} 