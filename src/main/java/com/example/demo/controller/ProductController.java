package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "产品管理", description = "产品相关的API接口")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "创建产品", description = "创建新的产品")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdProduct, "产品创建成功"));
    }

    @Operation(summary = "获取所有产品", description = "获取所有产品列表")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(summary = "分页获取产品", description = "分页获取产品列表")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProductsByPage(
            @Parameter(description = "页码，从0开始", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向", example = "desc") @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<ProductDTO> products = productService.getProductsByPage(pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(summary = "根据ID获取产品", description = "根据产品ID获取产品信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(
            @Parameter(description = "产品ID", example = "1") @PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(summary = "更新产品", description = "根据产品ID更新产品信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @Parameter(description = "产品ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "产品更新成功"));
    }

    @Operation(summary = "删除产品", description = "根据产品ID删除产品")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "产品ID", example = "1") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "产品删除成功"));
    }
}