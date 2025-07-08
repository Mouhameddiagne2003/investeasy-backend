package com.dic2.investeasy.repository;

import com.dic2.investeasy.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findByVideoId(String videoId, Pageable pageable);
    Page<Comment> findByParentId(String parentId, Pageable pageable);
    Page<Comment> findByReportedTrue(Pageable pageable);
}