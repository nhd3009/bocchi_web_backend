package com.bocchi.bocchiweb.util.mapper;

import com.bocchi.bocchiweb.dto.user.UserDTO;
import com.bocchi.bocchiweb.entity.User;

public class UserMapper {
    public static UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().getName() : null);
    }

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return user;
    }
}