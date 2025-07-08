package com.dic2.investeasy.service;

import com.dic2.investeasy.domain.Video;
import com.dic2.investeasy.dto.video.VideoCreateDTO;
import com.dic2.investeasy.dto.video.VideoUpdateDTO;
import com.dic2.investeasy.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;

    public Page<Video> getAllVideos(Pageable pageable) {
        return videoRepository.findByIsPublishedTrue(pageable);
    }

    public Page<Video> getVideos(String search, String category, String sort, Pageable pageable) {
        Sort sortBy = switch (sort) {
            case "oldest" -> Sort.by("createdAt").ascending();
            case "popular" -> Sort.by("views").descending();
            default -> Sort.by("createdAt").descending();
        };

        PageRequest sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortBy);

        if (search != null && !search.isEmpty() && category != null && !category.isEmpty()) {
            return videoRepository.findByTitleContainingOrDescriptionContainingAndCategory(search, search, category, sortedPageable);
        } else if (search != null && !search.isEmpty()) {
            return searchVideos(search, sortedPageable);
        } else if (category != null && !category.isEmpty()) {
            return getVideosByCategory(category, sortedPageable);
        } else {
            return getAllVideos(sortedPageable);
        }
    }

    public Page<Video> getVideosByCategory(String category, Pageable pageable) {
        return videoRepository.findByCategory(category, pageable);
    }

    public Page<Video> searchVideos(String query, Pageable pageable) {
        return videoRepository.findByTitleContainingOrDescriptionContaining(query, query, pageable);
    }

    public Video getVideoById(String id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }

    @Transactional
    public Video createVideo(VideoCreateDTO dto) {
        Video video = new Video();
        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setUrl(dto.getUrl());
        video.setThumbnail(dto.getThumbnail());
        video.setCategory(dto.getCategory());
        video.setDuration(dto.getDuration());
        video.setPublished(dto.getIsPublished());

        return videoRepository.save(video);
    }

    @Transactional
    public Video updateVideo(String id, VideoUpdateDTO dto) {
        Video video = getVideoById(id);

        if (dto.getTitle() != null) {
            video.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            video.setDescription(dto.getDescription());
        }
        if (dto.getUrl() != null) {
            video.setUrl(dto.getUrl());
        }
        if (dto.getThumbnail() != null) {
            video.setThumbnail(dto.getThumbnail());
        }
        if (dto.getCategory() != null) {
            video.setCategory(dto.getCategory());
        }
        if (dto.getDuration() != null) {
            video.setDuration(dto.getDuration());
        }
        if (dto.getIsPublished() != null) {
            video.setPublished(dto.getIsPublished());
        }

        return videoRepository.save(video);
    }

    @Transactional
    public void deleteVideo(String id) {
        Video video = getVideoById(id);
        videoRepository.delete(video);
    }

    @Transactional
    public Video incrementViews(String id) {
        Video video = getVideoById(id);
        video.setViews(video.getViews() + 1);
        return videoRepository.save(video);
    }

    @Transactional
    public Video toggleLike(String id) {
        Video video = getVideoById(id);
        video.setLikes(video.getLikes() + 1);
        return videoRepository.save(video);
    }

    public List<String> getCategories() {
        return videoRepository.findAllDistinctCategories();
    }
}