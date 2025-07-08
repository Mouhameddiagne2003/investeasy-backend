package com.dic2.investeasy.service;

import com.dic2.investeasy.controller.AdminController;
import com.dic2.investeasy.domain.User;
import com.dic2.investeasy.domain.UserRole;
import com.dic2.investeasy.dto.user.UserUpdateDTO;
import com.dic2.investeasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<User> getUsers(String search, String role, Pageable pageable) {
        if (search != null && role != null) {
            return userRepository.findByEmailContainingAndRole(search, UserRole.valueOf(role), pageable);
        } else if (search != null) {
            return userRepository.findByEmailContaining(search, pageable);
        } else if (role != null) {
            return userRepository.findByRole(UserRole.valueOf(role), pageable);
        }
        return userRepository.findAll(pageable);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User updateUser(String id, UserUpdateDTO dto) {
        User user = getUserById(id);

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.getIsActive() != null) {
            user.setActive(dto.getIsActive());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public AdminController.AdminStats getAdminStats() {
        AdminController.AdminStats stats = new AdminController.AdminStats();
        
        // User stats
        AdminController.AdminStats.UserStats userStats = new AdminController.AdminStats.UserStats();
        userStats.setTotal(userRepository.count());
        userStats.setNewThisMonth(countNewUsersThisMonth());
        userStats.setGrowth(calculateUserGrowth());
        stats.setUsers(userStats);

        // Video stats
        AdminController.AdminStats.VideoStats videoStats = new AdminController.AdminStats.VideoStats();
        videoStats.setTotal(0); // À implémenter
        videoStats.setTotalViews(0); // À implémenter
        videoStats.setNewThisMonth(0); // À implémenter
        stats.setVideos(videoStats);

        // Comment stats
        AdminController.AdminStats.CommentStats commentStats = new AdminController.AdminStats.CommentStats();
        commentStats.setTotal(0); // À implémenter
        commentStats.setNewThisMonth(0); // À implémenter
        stats.setComments(commentStats);

        // Recommendation stats
        AdminController.AdminStats.RecommendationStats recommendationStats = new AdminController.AdminStats.RecommendationStats();
        recommendationStats.setTotal(0); // À implémenter
        recommendationStats.setNewThisMonth(0); // À implémenter
        stats.setRecommendations(recommendationStats);

        return stats;
    }

    private long countNewUsersThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return userRepository.countByCreatedAtAfter(startOfMonth);
    }

    private double calculateUserGrowth() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        long lastMonthCount = userRepository.countByCreatedAtBefore(lastMonth);
        long thisMonthCount = countNewUsersThisMonth();
        
        if (lastMonthCount == 0) {
            return 0.0;
        }
        
        return ((double) thisMonthCount / lastMonthCount) * 100;
    }
} 