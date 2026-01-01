package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @NotBlank(message = "产品名称不能为空")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "产品描述不能为空")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double price;

    @NotNull(message = "库存不能为空")
    @Positive(message = "库存必须大于0")
    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus = ProductStatus.ACTIVE;

    @Column(name = "category")
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    public enum ProductStatus {
        ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED
    }
}