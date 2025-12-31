package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关的API接口")
public class UserController {

    private final UserService userService;

    @Operation(summary = "创建用户", description = "创建新的用户账户")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(
            @Valid @RequestBody UserDTO userDTO) {
        log.info("创建用户请求: {}", userDTO.getUsername());

        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdUser, "用户创建成功"));
    }

    @Operation(summary = "获取所有用户", description = "获取所有用户列表")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @Operation(summary = "分页获取用户", description = "分页获取用户列表")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getUsersByPage(
            @Parameter(description = "页码，从0开始", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向", example = "desc") @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);
        Page<UserDTO> users = userService.getUsersByPage(pageable);

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @Operation(summary = "根据ID获取用户", description = "根据用户ID获取用户信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @Parameter(description = "用户ID", example = "1") @PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(summary = "更新用户", description = "根据用户ID更新用户信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @Parameter(description = "用户ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {

        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "用户更新成功"));
    }

    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "用户ID", example = "1") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "用户删除成功"));
    }

    @Operation(summary = "获取活跃用户数量", description = "获取活跃用户的总数")
    @GetMapping("/stats/active-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getActiveUserCount() {
        long count = userService.countActiveUsers();
        Map<String, Long> response = Map.of("activeUsers", count);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "健康检查", description = "用户服务健康检查接口")
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("User service is running"));
    }
}