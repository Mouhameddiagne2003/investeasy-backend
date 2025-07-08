package com.dic2.investeasy.controller;

import com.dic2.investeasy.domain.Video;
import com.dic2.investeasy.dto.video.VideoCreateDTO;
import com.dic2.investeasy.dto.video.VideoUpdateDTO;
import com.dic2.investeasy.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<Page<Video>> getVideos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "newest") String sort,
            Pageable pageable) {
        return ResponseEntity.ok(videoService.getVideos(search, category, sort, pageable));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Video>> getAllVideos(Pageable pageable) {
        return ResponseEntity.ok(videoService.getAllVideos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Video> createVideo( @RequestBody VideoCreateDTO videoDTO) {
        return new ResponseEntity<>(videoService.createVideo(videoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Video> updateVideo(
            @PathVariable String id,
             @RequestBody VideoUpdateDTO videoDTO) {
        return ResponseEntity.ok(videoService.updateVideo(id, videoDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Video> incrementViews(@PathVariable String id) {
        return ResponseEntity.ok(videoService.incrementViews(id));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Video> toggleLike(@PathVariable String id) {
        return ResponseEntity.ok(videoService.toggleLike(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(videoService.getCategories());
    }
} 