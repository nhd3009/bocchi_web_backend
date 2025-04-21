package com.bocchi.bocchiweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bocchi.bocchiweb.dto.user.UserDTO;
import com.bocchi.bocchiweb.service.UserService;
import com.bocchi.bocchiweb.util.request.user.UpdateUserRequest;
import com.bocchi.bocchiweb.util.request.user.UserFilterRequest;
import com.bocchi.bocchiweb.util.response.ApiMessage;
import com.bocchi.bocchiweb.util.response.ApiResponse;
import com.bocchi.bocchiweb.util.response.PageResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    @ApiMessage("Get all user")
    public PageResponse<UserDTO> getAllUsers(UserFilterRequest request) {
        return userService.getAllUsers(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/users/{id}")
    @ApiMessage("Updating User")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        UserDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Update user successful!", updatedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Delete user successful!", null));
    }
}
