package com.bocchi.bocchiweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bocchi.bocchiweb.dto.user.UserDTO;
import com.bocchi.bocchiweb.entity.Role;
import com.bocchi.bocchiweb.entity.User;
import com.bocchi.bocchiweb.exception.ApiException;
import com.bocchi.bocchiweb.repository.RoleRepository;
import com.bocchi.bocchiweb.repository.UserRepository;
import com.bocchi.bocchiweb.util.error.UserErrorCode;
import com.bocchi.bocchiweb.util.mapper.UserMapper;
import com.bocchi.bocchiweb.util.request.user.RegisterUserRequest;
import com.bocchi.bocchiweb.util.request.user.UpdateUserRequest;
import com.bocchi.bocchiweb.util.request.user.UserFilterRequest;
import com.bocchi.bocchiweb.util.response.PageResponse;
import com.bocchi.bocchiweb.util.specification.UserSpecification;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public PageResponse<UserDTO> getAllUsers(UserFilterRequest request) {
        Specification<User> spec = Specification
                .where(UserSpecification.filterByUsername(request.getUsername()));

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<UserDTO> page = userRepository.findAll(spec, pageable).map(UserMapper::toDto);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());
    }

    public UserDTO registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ApiException(UserErrorCode.DEFAULT_ROLE_NOT_FOUND));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        user = userRepository.save(user);

        return UserMapper.toDto(user);
    }

    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user = userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
