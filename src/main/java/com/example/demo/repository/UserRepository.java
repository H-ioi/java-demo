package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 方法名查询（Spring Data JPA 自动实现）
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByUserType(User.UserType userType);

    List<User> findByIsActiveTrue();

    // 自定义查询
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();

    // 联合条件查询
    List<User> findByUserTypeAndIsActive(User.UserType userType, Boolean isActive);
}