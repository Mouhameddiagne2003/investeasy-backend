package com.dic2.investeasy.controller;

import com.dic2.investeasy.domain.Comment;
import com.dic2.investeasy.domain.User;
import com.dic2.investeasy.domain.Video;
import com.dic2.investeasy.dto.user.UserUpdateDTO;
import com.dic2.investeasy.dto.video.VideoCreateDTO;
import com.dic2.investeasy.dto.video.VideoUpdateDTO;
import com.dic2.investeasy.service.CommentService;
import com.dic2.investeasy.service.UserService;
import com.dic2.investeasy.service.VideoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final VideoService videoService;
    private final CommentService commentService;

    @GetMapping("/stats")
    public ResponseEntity<AdminStats> getStats() {
        return ResponseEntity.ok(userService.getAdminStats());
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(search, role, pageable));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/videos")
    public ResponseEntity<Page<Video>> getVideos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        return ResponseEntity.ok(videoService.getVideos(search, category, "newest", pageable));
    }

    @PostMapping("/videos")
    public ResponseEntity<Video> createVideo(@RequestBody VideoCreateDTO dto) {
        return ResponseEntity.ok(videoService.createVideo(dto));
    }

    @PutMapping("/videos/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable String id, @RequestBody VideoUpdateDTO dto) {
        return ResponseEntity.ok(videoService.updateVideo(id, dto));
    }

    @DeleteMapping("/videos/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments")
    public ResponseEntity<Page<Comment>> getComments(
            @RequestParam(required = false) Boolean reported,
            Pageable pageable) {
        if (reported != null && reported) {
            return ResponseEntity.ok(commentService.getReportedComments(pageable));
        }
        return ResponseEntity.ok(commentService.getAllComments(pageable));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id, null);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class AdminStats {
        private UserStats users;
        private VideoStats videos;
        private CommentStats comments;
        private RecommendationStats recommendations;

        @Data
        public static class UserStats {
            private long total;
            private long newThisMonth;
            private double growth;
        }

        @Data
        public static class VideoStats {
            private long total;
            private long totalViews;
            private long newThisMonth;
        }

        @Data
        public static class CommentStats {
            private long total;
            private long newThisMonth;
        }

        @Data
        public static class RecommendationStats {
            private long total;
            private long newThisMonth;
        }
    }
} 