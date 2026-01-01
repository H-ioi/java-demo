package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 创建产品
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName())) {
            throw new RuntimeException("产品名称已存在");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    // 获取所有产品
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 分页获取产品
    public Page<ProductDTO> getProductsByPage(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // 根据ID获取产品
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
        return convertToDTO(product);
    }

    // 更新产品
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));

        // 检查名称是否重复（排除当前产品）
        if (!product.getName().equals(productDTO.getName()) &&
                productRepository.existsByName(productDTO.getName())) {
            throw new RuntimeException("产品名称已存在");
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    // 删除产品
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("产品不存在");
        }
        productRepository.deleteById(id);
    }

    // 实体转DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }
}