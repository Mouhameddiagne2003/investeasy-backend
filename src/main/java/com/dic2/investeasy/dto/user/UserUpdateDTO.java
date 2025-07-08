package com.dic2.investeasy.dto.user;

import com.dic2.investeasy.domain.UserRole;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private UserRole role;
    private Boolean isActive;
} 