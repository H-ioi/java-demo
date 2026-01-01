package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "产品数据传输对象")
public class ProductDTO {
    @Schema(description = "产品ID", example = "1")
    private Long id;

    @NotBlank(message = "产品名称不能为空")
    @Schema(description = "产品名称", example = "iPhone 15")
    private String name;

    @NotBlank(message = "产品描述不能为空")
    @Schema(description = "产品描述", example = "最新款苹果手机")
    private String description;

    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    @Schema(description = "产品价格", example = "5999.99")
    private Double price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    @Schema(description = "产品库存", example = "100")
    private Integer stock;
}