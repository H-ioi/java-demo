package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "API信息", description = "API基本信息接口")
public class ApiController {

    @Operation(summary = "获取API信息", description = "获取API的基本信息和版本信息")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> getApiInfo() {
        Map<String, String> info = Map.of(
            "name", "Demo API",
            "version", "1.0.0",
            "description", "Spring Boot Demo Application",
            "basePath", "/api"
        );
        return ResponseEntity.ok(ApiResponse.success(info));
    }
}