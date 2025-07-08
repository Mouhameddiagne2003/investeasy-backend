package com.dic2.investeasy.repository;

import com.dic2.investeasy.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    Page<Video> findByCategory(String category, Pageable pageable);
    Page<Video> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);
    Page<Video> findByIsPublishedTrue(Pageable pageable);
    Page<Video> findByTitleContainingOrDescriptionContainingAndCategory(String title, String description, String category, Pageable pageable);
    
    @Query("SELECT DISTINCT v.category FROM Video v")
    List<String> findAllDistinctCategories();
} 