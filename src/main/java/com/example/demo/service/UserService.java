package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 创建用户
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("创建用户: {}", userDTO.getUsername());

        // 检查用户名是否已存在
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("邮箱已存在");
        }

        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .userType(userDTO.getUserType())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    // 获取所有用户
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 分页查询用户
    public Page<UserDTO> getUsersByPage(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // 根据ID获取用户
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + id));
    }

    // 更新用户
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + id));

        // 更新用户信息
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserType(userDTO.getUserType());
        user.setIsActive(userDTO.getIsActive());

        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    // 删除用户（软删除）
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + id));

        user.setIsDeleted(true);
        userRepository.save(user);
        log.info("用户已软删除: {}", id);
    }

    // 统计活跃用户
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }

    // 实体转DTO
    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .userType(user.getUserType())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}